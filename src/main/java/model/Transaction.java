package model;

import net.dv8tion.jda.api.entities.Message;

import java.time.LocalDateTime;

public class Transaction {
    public final LocalDateTime creationTime;
    public final CityMarket city;
    public final Product product;
    public final long amount;
    public Message message;

    public Transaction(CityMarket city, Product product, long amount){
        this.city = city;
        this.product = product;
        this.amount = amount;
        this.creationTime =  LocalDateTime.now();
    }

    public boolean process(){
        return product.processTransaction(this);
    }
}
