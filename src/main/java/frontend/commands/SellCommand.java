package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.Transaction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class SellCommand implements ICommand{
    private final String command = "sell";
    private Transaction transaction = null;


    @Override
    public String run(String[] args, MessageReceivedEvent event) {
        if(args.length != 3){
            return "Not the right amount of args. Usage: sell <city> <product> <amount>";
        }
        String checkTransactionResult = Transaction.checkTransactionData(args[0], args[1], args[2]);
        if(!checkTransactionResult.equalsIgnoreCase("Ok")){
            return checkTransactionResult;
        }
        int amount = Integer.parseInt(args[2]);
        CityMarket market = Bot.model.getMarket(args[0]);
        double price = market.getSellPrice(args[1], amount); //TODO: it would be prettier if the product calculates the price
        if(price == 0.0){
            return "There was an error. Please check if this product is available in this city."; //TODO: make custom errors for "product not found" etc, so the error message is more specific
        } else {
            transaction = new Transaction(market, market.getProduct(args[1]), amount);
            return "Do you really want to sell " + amount + " " + args[1] + " for "+String.format("%,.2f", price) + " GP in "+ args[0] + "?"; //TODO: make currency variable
        }

    }

    @Override
    public void callback(Message message){
        if(transaction != null) {
            message.addReaction("U+1F44D").queue();
            message.addReaction("U+1F44E").queue();
            Transaction trans = transaction;
            transaction = null;
            trans.message = message;
            Bot.pendingTransactions.put(message.getId(), trans);
        }
    }

    @Override
    public String getShortDesc() {
        return "Sell an amount of a product from a city. Usage: buy <city> <product> <amount>";
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
