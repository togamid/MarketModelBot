package frontend;

import frontend.commands.ICommand;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListeners extends ListenerAdapter {
    private final String botSignifier = "!";
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("test2");
        String msgContent = event.getMessage().getContentRaw();
        String mention;
        if(event.getMember() != null){
            mention = event.getMember().getAsMention();

        } else {
            mention = event.getAuthor().getAsMention();
        }

        if(!event.getAuthor().isBot() && (event.getChannel().getName().equals("bot-frontend.commands") || event.isFromType(ChannelType.PRIVATE)) && msgContent.startsWith(botSignifier)){
            String command;
            String args;
            int posSpace = msgContent.indexOf(' ');
            if(posSpace != -1 && posSpace+1 < msgContent.length()) {
                command = msgContent.substring(botSignifier.length(), posSpace);
                args = msgContent.substring(posSpace+1);
            }
            else {
                command = msgContent.substring(botSignifier.length());
                args="";
            }

            ICommand commandObj = Bot.commands.get(command);


            if(commandObj != null){
                event.getChannel().sendMessage(mention +" "+  commandObj.run(args, event)).queue();
            }
        }
    }
}
