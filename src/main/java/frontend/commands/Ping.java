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
        return "short description";
    }

    @Override
    public String getLongDesc() {
        return "long Description";
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
