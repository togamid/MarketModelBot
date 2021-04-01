package frontend.commands;

import model.Transaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.LinkedList;
import java.util.Queue;


public class BuyCommand extends BuySellCommand {
    private final String command = "buy";
    private final Queue<Transaction> transaction = new LinkedList<>();



    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        return createTransaction(args, true);
    }

    @Override
    public String getShortDesc() {
        return "Buy an amount of a product from a city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return "Allows you to buy the amount specified from the specified city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getCommand() {
        return command;
    }

}
