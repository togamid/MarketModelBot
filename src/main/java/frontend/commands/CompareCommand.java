package frontend.commands;

import frontend.Bot;
import frontend.response.BasicResponse;
import frontend.response.Response;
import frontend.response.TableResponse;
import model.CityMarket;
import model.Product;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CompareCommand implements ICommand {
    private final String[] header = {"City Name                    ","current stock","   max stock",
            "             buy price", "            sell price"};

    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            return new BasicResponse(getShortDesc());
        }
        String productName = Util.concat(args, 0, " ");
        List<String[]> products = new ArrayList<>();
        for(CityMarket city : Bot.model.getAllMarkets()){
            Product product = city.getProduct(productName);
            if(product != null){
                String[] info = product.getInfoAsStringArray();
                info[0] = city.getName();//replace product name with city name, as product name is obvious
                products.add(info);
            }
        }
        return new TableResponse(header, products.toArray(String[][]::new));
    }

    @Override
    public String getShortDesc() {
        return "Usage: compare <product name>. Returns information on the selected product in all cities where it is available";
    }

    @Override
    public String getLongDesc() {
        return getShortDesc();
    }

    @Override
    public String getCommand() {
        return "compare";
    }

    @Override
    public void init() {

    }

    @Override
    public void callback(Message m) {

    }
}
