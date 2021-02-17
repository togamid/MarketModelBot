package commands;

import model.CityMarket;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping implements ICommand{

    private final String command = "ping";
    @Override
    public String run(String args, MessageReceivedEvent event) {

        return ""  + CityMarket.loadCityMarket("/home/tobias/Desktop/temp.txt", true).getPriceofProduct(args);
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
}
