package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import model.DndPrice;
import model.Product;
import model.Transaction;
import net.dv8tion.jda.api.entities.Message;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BuySellCommand implements ICommand{
    protected final Queue<Transaction> transaction = new LinkedList<>();
    public static ConcurrentHashMap<String, Transaction> pendingTransactions = new ConcurrentHashMap<>();

    public static Transaction getTransaction(String messageID){
        return pendingTransactions.get(messageID);
    }

    public String createTransaction(String[] args, boolean isBuy) {
        int amount;
        double price;
        if(args.length < 3){
            return "Too few arguments. Usage: " + (isBuy?"buy":"sell")+ " <city> <product> <amount>";
        }
        CityMarket market = Bot.model.getMarket(args[0]);
        if(market == null){
            return "City " + args[0] + " not found!";
        }
        String productName = Util.concat(args, 1, " ");
        Product product = market.getProduct(productName);
        if(product == null){
            return "Product " + args[1] + " in city " + args[0] + " not found!";
        }

        try{
            amount = Integer.parseInt(args[args.length-1]);
            if(isBuy){
                amount *= -1;
            }
        } catch (NumberFormatException e){
            return "The amount has to be a positive Integer!";
        }

        String result = "";

        if(product.getCurrentStock() + amount > product.getMaxStock() ){
            amount = product.getMaxStock() - product.getCurrentStock();
            result += ("**Only space for " + amount + " available!** ");
        }
        if(product.getCurrentStock() < amount){
            amount = product.getCurrentStock();
            result += ("**Only " + amount + " available!**");
        }

        try{
            if(isBuy){
                price = product.getBuyPrice(amount*(-1)); //TODO: unify getBuyPrice and getSellPrice
            } else {
                price = product.getSellPrice(amount);
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }

        transaction.add(new Transaction(market, product, amount));
        return result + "Do you really want to "+ (isBuy?"buy ":"sell ") + (isBuy?amount*-1:amount) + " " + product.getName() + " for "+ DndPrice.getPrice(price, false) +" in "+ market.getName() + "?";
    }

    @Override
    public void callback(Message message){
        Transaction trans = transaction.remove();
        if(trans != null) {
            message.addReaction("U+1F44D").queue();
            message.addReaction("U+1F44E").queue();
            trans.message = message;
            pendingTransactions.put(message.getId(), trans);
        }
    }


    @Override
    public void init() {

    }
}
