package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand implements ICommand {

    String longDesc = "Zeigt diese Nachricht. !help <command> für weitere Informationen";
    String shortDesc = "Zeigt diese Nachricht. !help <command> für weitere Informationen";
    String command = "help";


    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) { //TODO: use the table for this

        if(args.length == 0) {
            StringBuilder builder = new StringBuilder("Verfügbare Kommandos: \n");
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
                StringBuilder builder = new StringBuilder("**"+Bot.botSignifier+currCommand.getCommand());
                builder.append("**: ");
                builder.append(currCommand.getLongDesc());
                return new BasicResponse( builder.toString());
            }
            else {
                return new BasicResponse( "Command nicht gefunden. Ohne das \""+ Bot.botSignifier+ "\" eingeben");
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
