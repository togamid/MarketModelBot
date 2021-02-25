package frontend.commands;

import frontend.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReloadNoSafeCommand implements ICommand{
    private final String commandName = "reloadWithoutSaving";
    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        Bot.model = Bot.dataConnector.loadModel();
        return null;
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
