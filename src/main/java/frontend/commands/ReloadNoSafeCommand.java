package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReloadNoSafeCommand implements ICommand{
    private final String commandName = "reloadWithoutSaving";
    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) {
        String permission = Util.checkPermission(event);
        if(permission != null){
            return new BasicResponse(permission);
        }
        Bot.model = Bot.dataConnector.loadModel();
        return new BasicResponse( "reloaded the data");
    }

    @Override
    public String getShortDesc() {
        return "Loads the data without saving. Used to load manual changes to the data. Might lose some data.";
    }

    @Override
    public String getLongDesc() {
        return "Loads the data without saving. Used to load manual changes to the data. Might lose some data.";
    }

    @Override
    public String getCommand() {
        return commandName;
    }

    @Override
    public void init() {

    }

    @Override
    public void callback(Message m) {

    }
}
