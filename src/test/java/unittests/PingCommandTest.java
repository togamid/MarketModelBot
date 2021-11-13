package unittests;

import frontend.commands.Ping;
import frontend.response.BasicResponse;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PingCommandTest {

    @Mock
    private MessageReceivedEvent event;

    private Ping ping;

    @Test
    void runPing(){
        this.ping = new Ping();
        BasicResponse response = ping.run(new String[0], event );
        assertTrue(response.getMessageString().equals("pong"));

    }
}
