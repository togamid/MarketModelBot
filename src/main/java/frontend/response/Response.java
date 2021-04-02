package frontend.response;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;


public abstract class Response {

    public abstract boolean send(TextChannel channel, String mention, Consumer<Message> callback);

    public abstract String getMessageString();

}
