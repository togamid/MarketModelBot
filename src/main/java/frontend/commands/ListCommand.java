package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import frontend.response.Response;
import frontend.response.TableResponse;
import model.CityMarket;
import model.DndPrice;
import model.Product;
import model.exceptions.NoStorageException;
import model.exceptions.ProductNotAvailableException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class ListCommand implements ICommand{
    public final String commandName = "list";
    private final String[] header = {"Name                       ","current stock","   max stock",
             "           buy price", "          sell price"};
    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            StringBuilder builder = new StringBuilder("List of all cities. Use !list <city> to see more information.");
            for (CityMarket market : Bot.model.getAllMarkets()){
                builder.append("\n");
                builder.append( market.getName());
            }
            return new BasicResponse(builder.toString());
        }
        else if(args.length == 1){
            CityMarket city = Bot.model.getMarket(args[0]);
            if(city == null){
                return new BasicResponse("City " + args[0]+ " not found!");
            }
            Product[] products = city.getAllProducts();
            Arrays.sort(products);
            return new TableResponse(header, getAsStringArray(products));
        }
        else {
            String productname = Util.concat(args,1, " ");
            CityMarket city = Bot.model.getMarket(args[0]);
            if(city == null){
                return new BasicResponse("City " + args[0]+ " not found!");
            }
            Product product = city.getProduct(productname);
            if(product == null){
                return new BasicResponse("Product " + args[1] + " in city "+ args[0]+ " not found!");
            }

            return new TableResponse(header, getAsStringArray(new Product[]{product}));
        }
    }

    private String[][] getAsStringArray(Product[] products){
        String[][] response = new String[products.length][];
        for(int i = 0; i<response.length; i++){
            response[i] = new String[header.length];
            response[i][0] = products[i].getName();
            response[i][1] = Integer.toString(products[i].getCurrentStock());
            response[i][2] = Integer.toString(products[i].getMaxStock());
            String buyPrice;
            try{
                buyPrice = DndPrice.getPrice(products[i].getBuyPrice(1), true);
            }
            catch (ProductNotAvailableException e){
                buyPrice = "N/A";
            }
            response[i][3] = buyPrice;
            String sellPrice;
            try{
                sellPrice = DndPrice.getPrice(products[i].getSellPrice(1), true);
            }
            catch (NoStorageException e){
                sellPrice = "N/A";
            }
            response[i][4] =  sellPrice;
        }
        return response;
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
