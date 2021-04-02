package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoadCommand implements ICommand{
    private final String command = "reload";

    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) {
        String permission = Util.checkPermission(event);
        if(permission != null){
            return new BasicResponse(permission);
        }

       Bot.dataConnector.safeModel(Bot.model);
       Bot.model = Bot.dataConnector.loadModel(); //TODO: this doesn't add new cities and just updates the old ones
        return new BasicResponse( "Successfully reloaded the data");
    }

    @Override
    public String getShortDesc() {
        return "Safes and reloads the data. Used for adding new cities";
    }

    @Override
    public String getLongDesc() {
        return "Safes and reloads the data. Used for adding new cities";
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
