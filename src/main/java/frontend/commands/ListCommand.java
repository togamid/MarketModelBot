package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import frontend.response.Response;
import frontend.response.TableResponse;
import model.CityMarket;
import model.Product;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class ListCommand implements ICommand{
    public final String commandName = "list";
    private final String[] header = {"Name                         ","current stock","   max stock",
             "             buy price", "            sell price"};
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
            Product[] product = city.searchProduct(productname);
            if(product.length == 0){
                return new BasicResponse("Product " + productname + " in city "+ args[0]+ " not found!");
            }

            return new TableResponse(header, getAsStringArray(product));
        }
    }

    private String[][] getAsStringArray(Product[] products){
        String[][] response = new String[products.length][];
        for(int i = 0; i<response.length; i++){
            response[i] = products[i].getInfoAsStringArray();
            if(header.length != response[i].length){
                throw new RuntimeException("The header of the list is invalid!");
            }
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
