package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.Product;
import model.Transaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;



public class BuyCommand implements ICommand {
    private final String command = "buy";
    private Transaction transaction = null;



    @Override
    public String run(String[] args, MessageReceivedEvent event) {

        int amount;
        double price;
        if(args.length != 3){
            return "Not the right amount of args. Usage: buy <city> <product> <amount>";
        }
        CityMarket market = Bot.model.getMarket(args[0]);
        if(market == null){
            return "City " + args[0] + " not found!";
        }
        Product product = market.getProduct(args[1]);
        if(product == null){
            return "Product " + args[1] + " in city " + args[0] + " not found!";
        }

        try{
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
            return "The amount has to be a positive Integer!";
        }

        try{
            price = product.getBuyPrice(amount);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }

        transaction = new Transaction(market, product, amount * (-1));
        return "Do you really want to buy " + amount + " " + args[1] + " for "+String.format("%,.2f", price) + " " + Bot.config.get("Currency") +" in "+ args[0] + "?";
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
