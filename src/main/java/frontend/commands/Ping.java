package frontend.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements ICommand{

    private final String command = "ping";
    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        return "pong";
    }

    @Override
    public String getShortDesc() {
        return "Pings the bot to see if it is still active";
    }

    @Override
    public String getLongDesc() {
        return "Pings the bot to see if it is still active";
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void init() {

    }
    @Override
    public void callback(Message m){

    }
}
