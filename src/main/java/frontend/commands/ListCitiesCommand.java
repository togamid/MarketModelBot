package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import frontend.response.Response;
import model.CityMarket;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListCitiesCommand implements ICommand{
    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        if(args.length != 0){
            System.out.println(getShortDesc());
        }
        StringBuilder builder = new StringBuilder("List of all available cities.");
        for (CityMarket market : Bot.model.getAllMarkets()){
            builder.append("\n");
            builder.append( market.getName());
        }
        return new BasicResponse(builder.toString());
    }

    @Override
    public String getShortDesc() {
        return "Usage: listCities . Lists all available cities";
    }

    @Override
    public String getLongDesc() {
        return getShortDesc();
    }

    @Override
    public String getCommand() {
        return "listCities";
    }

    @Override
    public void init() {

    }

    @Override
    public void callback(Message m) {

    }
}
