package frontend;

import frontend.commands.BuySellCommand;
import frontend.commands.ICommand;
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
             //TODO: fix this properly
            String[] responses = commandObj.run(args, event).split("\t");
            event.getChannel().sendMessage(mention +" "+ responses[0]).queue(m -> commandObj.callback(m));

                for(int i = 1; i< responses.length; i++){
                    event.getChannel().sendMessage(responses[i]).queue(m -> commandObj.callback(m));
                }
            } else {
                System.out.println("Warning: command " + command + " not found!");
            }
        }
    }

    private String[] splitMessage(String originalmessage){
        return originalmessage.split("\t");
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        String reaction = event.getReaction().getReactionEmote().getAsCodepoints();
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
    }
}
