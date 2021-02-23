package frontend;

import data.DataConnector;
import frontend.commands.*;

import model.Model;
import model.Transaction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Bot {
    public static JDA jda;
    public static HashMap<String, ICommand> commands = new HashMap<>();
    private static final ICommand[] commandArray = {new Ping(), new BuyCommand(), new SellCommand(), new SafeCommand()};
    public static Model model;
    public static ConcurrentHashMap<String, Transaction> pendingTransactions = new ConcurrentHashMap<>();
    public static DataConnector dataConnector;
    public static final String botSignifier = "!";

    public void init(){
        /* Config config = new Config();
        config.loadConfig("config.txt");*/
        try {
            jda = JDABuilder.createDefault("Token")
                    .addEventListeners(new EventListeners())
                    .build();
            jda.awaitReady();
        }
        catch (Exception e){
           System.out.println(e.getMessage());
        }
        dataConnector = new DataConnector("/home/sibylle/Schreibtisch", new String[] {"temp"}); //TODO: load path dynamicly
        model = dataConnector.loadModel();

        for (ICommand command : commandArray) {
            command.init();
            commands.put(command.getCommand(), command);
        }

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(TimeThread::run, 0, 10, TimeUnit.SECONDS);


    }
}
