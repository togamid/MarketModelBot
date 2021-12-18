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
    private final String categoryIdentifier = "cat";
    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        if(args.length <= 1){
            return new BasicResponse("!list: Usage: !list [prod or cat] <city> [prod/cat name] Lists products in a city. Use cat to search for categories, prod for product name. For a list of cities, please use !listCities");
        }

        boolean listCategories = args[0].equalsIgnoreCase(categoryIdentifier);
        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);
        return list(listCategories, remainingArgs, event);
    }

    protected Response list(boolean byCategory, String[] args, MessageReceivedEvent event){
        if(args.length < 1){
            return new BasicResponse("Usage: list [prod/cat] <city> [name]. For a list of cities, please use listCities");
        }

        String cityName = args[0];

        if(args.length == 1){
            CityMarket city = Bot.model.getMarket(cityName);
            if(city == null){
                return new BasicResponse("City " + cityName+ " not found!");
            }
            Product[] products = city.getAllProducts();
            Arrays.sort(products);
            return new TableResponse(header, getAsStringArray(products));
        }
        else {
            String productname = Util.concat(args,1, " ");
            CityMarket city = Bot.model.getMarket(cityName);
            if(city == null){
                return new BasicResponse("City " + cityName+ " not found!");
            }
            Product[] products;
            if(!byCategory) {
                products = city.searchProduct(productname);
            }
            else {
                products = city.searchProductByCategory(productname);
            }
            if(products.length == 0){
                return new BasicResponse( "Product " + productname + " in city "+ cityName+ " not found!");
            }

            return new TableResponse(header, getAsStringArray(products));
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
        return  "Usage: !list [prod/cat] <city> [name] Lists products in a city. Use cat to search for categories, prod for product name.";
    }

    @Override
    public String getLongDesc() {
        return getShortDesc();
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
