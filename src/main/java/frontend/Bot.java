package frontend;

import frontend.commands.BuyCommand;
import frontend.commands.ICommand;
import frontend.commands.Ping;
import frontend.commands.SellCommand;
import model.CityMarket;
import model.Transaction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Bot {
    public static JDA jda;
    public static HashMap<String, ICommand> commands = new HashMap<>();
    private static final ICommand[] commandArray = {new Ping(), new BuyCommand(), new SellCommand()};
    public static CityMarket market; //TODO: make a model class and allow for more than one city
    public static ConcurrentHashMap<String, Transaction> pendingTransactions = new ConcurrentHashMap<>();
    public void init(){
        /* Config config = new Config();
        config.loadConfig("config.txt");*/
        try {
            jda = JDABuilder.createDefault("Token")
                    .addEventListeners(new EventListeners())
                    .build();
            jda.awaitReady();
        } catch (Exception e){
           System.out.println(e.getMessage());
        }
        market = CityMarket.loadCityMarket("/home/sibylle/Schreibtisch/temp.txt", true); //TODO: load path dynamicly

        for (ICommand command : commandArray) {
            command.init();
            commands.put(command.getCommand(), command);
        }
    }
}
