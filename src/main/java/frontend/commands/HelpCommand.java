package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand implements ICommand {

    String shortDesc = "Shows this message. "+Bot.botSignifier +"help <command> for more information";
    String longDesc = shortDesc;
    String command = "help";


    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) { //TODO: use the table for this

        if(args.length == 0) {
            StringBuilder builder = new StringBuilder("Available commands: \n");
            ICommand[] commands =  Bot.commandArray;

            for (int i = 0; i< commands.length; i++){
                ICommand currCommand = commands[i];
                builder.append("**" + Bot.botSignifier);
                builder.append(currCommand.getCommand());
                builder.append("**: ");
                builder.append(currCommand.getShortDesc());
                builder.append("\n");
            }
            return new BasicResponse(builder.toString());
        }
        else {
            ICommand currCommand = Bot.commands.get(args[0]);
            if(currCommand != null) {
                String builder = "**" + Bot.botSignifier + currCommand.getCommand() + "**: " + currCommand.getLongDesc();
                return new BasicResponse(builder);
            }
            else {
                return new BasicResponse( "Command found. Please enter it without the \""+ Bot.botSignifier+ "\".");
            }
        }
    }

    @Override
    public String getShortDesc() {
        return shortDesc;
    }

    @Override
    public String getLongDesc() {
        return longDesc;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void init() {
    }

    @Override
    public void callback(Message m) {
    }
}
