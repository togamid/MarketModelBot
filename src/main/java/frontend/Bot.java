package frontend;

import data.DataConnector;
import frontend.commands.*;

import model.AutosafeThread;
import model.Model;
import model.TimeThread;
import model.Transaction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {
    public static JDA jda;
    public static HashMap<String, ICommand> commands = new HashMap<>();
    private static final ICommand[] commandArray = {new Ping(), new BuyCommand(), new SellCommand(), new SafeCommand(), new HelpCommand()};
    public static Model model;
    public static ConcurrentHashMap<String, Transaction> pendingTransactions = new ConcurrentHashMap<>();
    public static DataConnector dataConnector;
    public static final Config config = new Config("config.txt");
    public static final String botSignifier = config.get("BotSignifier");
    public static final double npcTraderMargin = Double.parseDouble(config.get("NpcTraderMargin"));

    public void init(){
        try {
            jda = JDABuilder.createDefault(config.get("Token"))
                    .addEventListeners(new EventListeners())
                    .build();
            jda.awaitReady();
        }
        catch (Exception e){
           System.out.println(e.getMessage());
        }
        dataConnector = new DataConnector(config.get("Path"), config.getCities());
        model = dataConnector.loadModel();

        for (ICommand command : commandArray) {
            command.init();
            commands.put(command.getCommand(), command);
        }

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(TimeThread::run, 0, 20, TimeUnit.SECONDS);

        final ScheduledExecutorService autosafeService = Executors.newSingleThreadScheduledExecutor();
        autosafeService.scheduleAtFixedRate(AutosafeThread::run, 10, Integer.parseInt( config.get("AutosafeTime(Minutes)")), TimeUnit.MINUTES);

        //TODO: prices in GP, SP, CP -> as Int in CP
        //TODO: list command that lists products in a city
    }
}
