package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.DndPrice;
import model.Product;
import model.exceptions.ProductNotAvailableException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListCommand implements ICommand{
    public final String commandName = "list";
    private final int paddingLength = 15;
    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            StringBuilder builder = new StringBuilder("List of all cities. Use !list <city> to see more information.");
            for (CityMarket market : Bot.model.getAllMarkets()){
                builder.append("\n");
                builder.append( market.getName());
            }
            return builder.toString();
        } else if(args.length == 1){
            CityMarket city = Bot.model.getMarket(args[0]);
            if(city == null){
                return "City " + args[0]+ " not found!";
            }
            StringBuilder builder = new StringBuilder("List of all products in " + city.getName()+":");
            builder.append("\nName");
            for (String header: new String[]{"current stock", "production", "consumption", "buy price" }) {
                builder.append(String.format("%1$" + paddingLength + "s", header));
            }
            for (Product product : city.getAllProducts()){
                builder.append("\n");
                builder.append( product.getName());
                builder.append(":\t");
                builder.append(String.format("%1$" + paddingLength + "s", product.getCurrentStock()));
                builder.append("\t");
                builder.append(String.format("%1$" + paddingLength + "s", product.production));
                builder.append("\t");
                builder.append(String.format("%1$" + paddingLength + "s", product.consumption));
                builder.append("\t");
                String price;
                try{
                    price = DndPrice.getPrice(product.getBuyPrice(1));
                }
                catch (ProductNotAvailableException e){
                    price = "N/A";
                }
                builder.append(String.format("%1$" + (paddingLength + 15) + "s", price));
                builder.append("\t");
            }
            return builder.toString();
        }
        return "Too many arguments. Usage: \"list\" ord \"list <city>\"";
    }

    @Override
    public String getShortDesc() {
        return "Lists all cities. Lists all products of a city with \"list <city>\"";
    }

    @Override
    public String getLongDesc() {
        return null;
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
