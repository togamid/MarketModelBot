package frontend.commands;

import frontend.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutdownCommand implements ICommand{
    private final String commandName = "shutdown";
    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        String permission = Util.checkPermission(event);
        if(permission != null){
            return permission;
        }
        Bot.shutdown();
        return "Shutting down the bot!";
    }

    @Override
    public String getShortDesc() {
        return null;
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
