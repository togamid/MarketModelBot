package frontend.commands;

import frontend.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoadCommand implements ICommand{
    private final String command = "reload";

    @Override
    public String run(String[] args, MessageReceivedEvent event) {
       Bot.dataConnector.safeModel(Bot.model);
       Bot.model = Bot.dataConnector.loadModel();
        return "Successfully reloaded the data";
    }

    @Override
    public String getShortDesc() {
        return "Safes and reloads the data. Used for adding new cities";
    }

    @Override
    public String getLongDesc() {
        return "Safes and reloads the data. Used for adding new cities";
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void init() {
    }

    @Override
    public void callback(Message m) {

    }
}
