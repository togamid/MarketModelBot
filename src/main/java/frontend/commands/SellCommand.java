package frontend.commands;

import model.Transaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.LinkedList;
import java.util.Queue;


public class SellCommand extends BuySellCommand{
    private final String command = "sell";
    private final Queue<Transaction> transaction = new LinkedList<>();


    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        return createTransaction(args, false);
    }


    @Override
    public String getShortDesc() {
        return "Sell an amount of a product from a city. Usage: sell <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return "Allows you to sell the amount specified to the specified city. Usage: sell <city> <product> <amount>";
    }

    @Override
    public String getCommand() {
        return command;
    }

}
