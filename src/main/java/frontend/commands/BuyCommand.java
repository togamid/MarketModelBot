package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.Transaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;

import java.util.LinkedList;
import java.util.Queue;


public class BuyCommand implements ICommand {
    private final String command = "buy";
    private Queue<Transaction> transactions = new LinkedList<>(); //just storing the transactions in a queue might cause problems



    @Override
    public String run(String argumentsString, MessageReceivedEvent event) {
        String[] args = argumentsString.split(" ");
        int amount = 0;
        if(args.length != 3){
            return "Not the right amount of args. Usage: buy <city> <product> <amount>";
        }
        try{
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
            return "The amount has to be an integer number";
        }
        double price = Bot.market.getBuyPrice(args[1], amount); //TODO: adjust for multiple markets
        if(price == 0.0){
            return "There was an error. Please check if this product is available in this city."; //TODO: make custom errors for "product not found" etc, so the error message is more specific
        } else {
            transactions.add(new Transaction(Bot.market, Bot.market.getProduct(args[1]), amount * (-1) )); //TODO: adjust for multiple markets
            return "Do you really want to buy " + amount + " " + args[1] + " for "+String.format("%,.2f", price) + " GP in "+ args[0] + "?"; //TODO: make currency variable, format price
        }

    }

    @Override
    public void callback(Message message){
        if(!transactions.isEmpty()) {
            message.addReaction("U+1F44D").queue();
            message.addReaction("U+1F44E").queue();
            Transaction trans = transactions.remove();
            trans.message = message;
            Bot.pendingTransactions.put(message.getId(), trans);
        }
    }

    @Override
    public String getShortDesc() {
        return "Buy an amount of a product from a city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return null;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void init() {

    }
}
