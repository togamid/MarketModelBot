package frontend.commands;

import frontend.response.Response;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ICommand {
    Response run(String[] args, MessageReceivedEvent event);
    String getShortDesc();
    String getLongDesc();
    String getCommand();
    void init();
    void callback(Message m);
}
