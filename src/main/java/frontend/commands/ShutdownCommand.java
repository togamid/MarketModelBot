package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutdownCommand implements ICommand{
    private final String commandName = "shutdown";
    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) {
        String permission = Util.checkPermission(event);
        if(permission != null){
            return new BasicResponse(permission);
        }
        return new BasicResponse("Shutting down the bot!");
    }

    @Override
    public String getShortDesc() {
        return "Shuts down the bot";
    }

    @Override
    public String getLongDesc() {
        return "Shuts down the bot";
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
        Bot.shutdown();
    }
}
