package frontend;

import frontend.commands.ICommand;
import frontend.commands.Ping;
import model.CityMarket;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.HashMap;

public class Bot {
    public static JDA jda;
    public static HashMap<String, ICommand> commands = new HashMap<>();
    private static final ICommand[] commandArray = {new Ping()};
    public static CityMarket market; //TODO: make a model class and allow for more than one city
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
        market = CityMarket.loadCityMarket("/home/tobias/Desktop/temp.txt", true); //TODO: load path dynamicly

        for (ICommand command : commandArray) {
            command.init();
            commands.put(command.getCommand(), command);
        }
    }
}
