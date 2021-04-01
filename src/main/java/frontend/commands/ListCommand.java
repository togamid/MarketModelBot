package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.DndPrice;
import model.Product;
import model.exceptions.NoStorageException;
import model.exceptions.ProductNotAvailableException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListCommand implements ICommand{
    public final String commandName = "list";
    private final int paddingLength = -15;
    private final int pricePaddingLength = -20;
    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            StringBuilder builder = new StringBuilder("List of all cities. Use !list <city> to see more information.");
            for (CityMarket market : Bot.model.getAllMarkets()){
                builder.append("\n");
                builder.append( market.getName());
            }
            return builder.toString();
        }
        else if(args.length == 1){
            CityMarket city = Bot.model.getMarket(args[0]);
            if(city == null){
                return "City " + args[0]+ " not found!";
            }
            StringBuilder builder = new StringBuilder("List of all products in " + city.getName()+":");
            builder.append(formatResponse(city.getAllProducts()));
            return builder.toString();
        }
        else if(args.length >= 2){
            String productname = args[1];
            for(int i = 2; i< args.length; i++){
                productname += (" " +args[i]);
            }
            CityMarket city = Bot.model.getMarket(args[0]);
            if(city == null){
                return "City " + args[0]+ " not found!";
            }
            Product product = city.getProduct(productname);
            if(product == null){
                return "Product " + args[1] + " in city "+ args[0]+ " not found!";
            }

            return formatResponse(new Product[]{product});
        }
        return "Too many arguments. Usage: \"list\" or \"list <city>\"";
    }

    private String formatResponse(Product[] products){
        StringBuilder builder = new StringBuilder();
        builder.append("\n```");
        for (String header: new String[]{"Name","current stock","max stock", "production", "consumption"}) {
            builder.append(String.format("%1$" + paddingLength + "s", header));
        }
        builder.append(String.format("%1$" + pricePaddingLength + "s", "buy price"));
        builder.append(String.format("%1$" + pricePaddingLength + "s", "sell price"));
        for (Product product : products){
            builder.append("\n");
            if(builder.length() - builder.lastIndexOf("\t") > 1700){
                builder.append("```\t```");
            }
            builder.append(String.format("%1$" + paddingLength + "s", product.getName() + ":"));
            builder.append(String.format("%1$" + paddingLength + "s", product.getCurrentStock()));
            builder.append(String.format("%1$" + paddingLength + "s", product.getMaxStock()));
            builder.append(String.format("%1$" + paddingLength + "s", product.production));
            builder.append(String.format("%1$" + paddingLength + "s", product.consumption));
            String buyPrice;
            try{
                buyPrice = DndPrice.getPrice(product.getBuyPrice(1), true);
            }
            catch (ProductNotAvailableException e){
                buyPrice = "N/A";
            }
            builder.append(String.format("%1$" + pricePaddingLength + "s", buyPrice));
            String sellPrice;
            try{
                sellPrice = DndPrice.getPrice(product.getSellPrice(1), true);
            }
            catch (NoStorageException e){
                sellPrice = "N/A";
            }
            builder.append(String.format("%1$" + pricePaddingLength + "s", sellPrice));


        }
        builder.append("```");
        return builder.toString();
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
