package model;

import frontend.Bot;
import net.dv8tion.jda.api.entities.Message;

import java.time.LocalDateTime;

public class Transaction {
    public final LocalDateTime creationTime;
    public final CityMarket city;
    public final Product product;
    public final int amount;
    public Message message;

    public Transaction(CityMarket city, Product product, int amount){
        this.city = city;
        this.product = product;
        this.amount = amount;
        this.creationTime =  LocalDateTime.now();
    }

    public boolean process(){
        return product.processTransaction(this);
    }

    public static String checkTransactionData(String cityName, String productName, String amountString){
        CityMarket market = Bot.model.getMarket(cityName);
        if(market == null){
            return "City " + cityName + " not found!";
        }
        Product product = market.getProduct(productName);
        if(product == null){
            return "Product " + productName + " in city " + cityName + " not found!";
        }

        int amount = 0;
        try{
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e){
            return "The amount has to be an integer number";
        }

        if(amount < 0){
            return "The amount may not be negative. Please use buy/sell";
        }

        return "Ok";
    }
}
