package frontend;

import frontend.commands.BuySellCommand;
import frontend.commands.ICommand;
import frontend.response.TableResponse;
import model.Transaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class EventListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String msgContent = event.getMessage().getContentRaw();
        String mention;
        if(event.getMember() != null){
            mention = event.getMember().getAsMention();

        } else {
            mention = event.getAuthor().getAsMention();
        }

        if(!event.getAuthor().isBot() && event.getChannel().getName().equals(Bot.config.get("BotChannel")) && msgContent.startsWith(Bot.botSignifier)){
            String command;
            String[] args;
            int posSpace = msgContent.indexOf(' ');
            if(posSpace != -1 && posSpace+1 < msgContent.length()) {
                command = msgContent.substring(Bot.botSignifier.length(), posSpace);
                args = msgContent.substring(posSpace+1).split(" ");
            }
            else {
                command = msgContent.substring(Bot.botSignifier.length());
                args= new String[0];
            }

            ICommand commandObj = Bot.commands.get(command);
            if(commandObj != null){
                commandObj.run(args, event).send(event.getTextChannel(), mention, commandObj::callback);
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        String reaction = event.getReaction().getReactionEmote().getAsCodepoints();
        if(event.getUser().isBot()){
            return;
        }
        switch(reaction.toUpperCase()){
            case "U+1F44D":
            case "U+1F44E":
                //TODO: rework this
                Transaction transaction = BuySellCommand.getTransaction(event.getMessageId()); //TODO: This can be more performant if the lookup is after checking the emoji
                if(transaction != null && event.getUserIdLong() == transaction.message.getMentionedUsers().get(0).getIdLong()) {
                    if(reaction.equalsIgnoreCase("U+1F44D")){
                        if(transaction.process()){
                            transaction.message.addReaction("U+2705").queue();
                        } else {
                            transaction.message.addReaction("U+274E").queue();
                            event.getChannel().sendMessage(event.getUser().getAsMention()+ " the transaction could not be processed. Please try again!").queue();
                        }
                        BuySellCommand.pendingTransactions.remove(event.getMessageId());
                    } else if(reaction.equalsIgnoreCase("U+1F44E")){
                        transaction.message.addReaction("U+274E").queue();
                        BuySellCommand.pendingTransactions.remove(event.getMessageId());
                    }

                }
                break;
            case TableResponse.forwardReaction:
                TableResponse table = TableResponse.pendingTables.get(event.getMessageId());
                if(table != null){
                    event.getReaction().removeReaction(event.getUser()).queue();
                    table.scrollForward();
                }
                break;
            case TableResponse.backwardReaction:
                table = TableResponse.pendingTables.get(event.getMessageId());
                if(table != null){
                    event.getReaction().removeReaction(event.getUser()).queue();
                    table.scrollBackward();
                }
                break;

        }
    }
}
