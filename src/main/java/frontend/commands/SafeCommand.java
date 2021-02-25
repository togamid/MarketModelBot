package frontend.commands;

import frontend.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SafeCommand implements ICommand{
    private final String commandName = "safe";

    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        String permission = Util.checkPermission(event);
        if(permission != null){
            return permission;
        }
        if(Bot.dataConnector.safeModel(Bot.model)){
            return "Safed successfully!";
        } else {
            return "An error occured during saving. The data might not or only partially have been saved";
        }
    }

    @Override
    public String getShortDesc() {
        return "safes the current state of the model";
    }

    @Override
    public String getLongDesc() {
        return null;
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
