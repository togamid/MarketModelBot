package frontend.response;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;

/**
 * A class used to send simple response messages which are no longer than 1999 characters
 */
public class BasicResponse extends Response{

    private final String message;

    public BasicResponse(String message){
        this.message = sanitize(message);
    }

    @Override
    public boolean send(TextChannel channel, String mention, Consumer<Message> callback) {

        if(message.length() >1999){
            System.out.println("[Error] [BasicResponse] The message was too long to be sent!");
            return false;
        }
        else {
            channel.sendMessage(mention + " " + message ).queue(callback);
            return true;
        }
    }

    @Override
    public String getMessageString() {
        return message;
    }

    private String sanitize(String message) {
        return message.replace("@", "(at)");
    }
}
