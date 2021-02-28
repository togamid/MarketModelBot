package model;


import frontend.Bot;
import model.exceptions.NoStorageException;
import model.exceptions.ProductNotAvailableException;

public class Product {
    public static final int numberProperties = 7;
    public final String name;
    public final double production; //how much is produced each day
    public final double consumption; //how much is consumed each day
    public final double minPrice; //the minimum price possible. Is approached asymptotically
    public final double maxPrice; //the maximum price
    private double currentStock;
    private final int maxStock; //the maximum amount the city can store of this good. Also used to scale the price-development to the expected quantity




    public Product(String name, double production, double consumption, int maxStock, double minPrice, double maxPrice, double currentStock){
        this.name = name;
        this.production = production;
        this.consumption = consumption;
        this.maxStock = maxStock;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currentStock = currentStock;
    }

    public String getName(){
        return name;
    }

    public int getCurrentStock(){
        return (int)currentStock;
    }
    public double getExactCurrentStock(){
        return currentStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public boolean processTransaction(Transaction trans){
        double newStock = currentStock + trans.amount;
        if(newStock < 0 || newStock > maxStock) {
            return false;
        }
        else {
            currentStock = currentStock + trans.amount;
            return true;
        }

    }

    public double getPriceAtStock(int stock){

        //Model using 1/x as price curve
       /* double tmpConsum = 0.2;
        if(consumption != 0) {
            tmpConsum = consumption;
        }

        double tmpProduct = 0.2;
        if(production != 0) {
            tmpProduct = production;
        }
        double xAxisStretching = (tmpProduct/tmpConsum)* 16.0/(maxStock); //if there is more production than consumption, squash the graph on the x-Axis. Use maxStock to calibrate for the expected amount
        double yAxisStretching = priceVolatilityFactor; //scale the price increase with the priceDevelopmentFactor
        return (yAxisStretching/(stock*xAxisStretching + 0.5)) + minPrice; */

        double minPrice = this.minPrice;
        double maxPrice = this.maxPrice;
        double minStock = 0;
        double maxStock = this.maxStock;

        double price;
        if(stock > maxStock){
            price = minPrice;
        }
        else if (stock < minStock){
            price = maxPrice;
        }
        else {
            price = maxPrice + ((minPrice - maxPrice)/(maxStock - minStock))*stock;
        }

        return price;
    }

    public double getBuyPrice(int amount) throws ProductNotAvailableException{
        double sum = 0;
        if(amount <0 ){
            System.out.println("Error: Amount to buy may not be negative!");
            throw new NumberFormatException("Amount to buy may not be negative!");
        }
        if(currentStock - amount < 0){
            System.out.println("Error: Not enough available!");
            throw new ProductNotAvailableException("Not enough available to complete transaction!");
        }
        for(int i = 0; i<amount; i++){
            sum += getPriceAtStock(getCurrentStock()-i);
        }
        return sum * Bot.npcTraderMargin;
    }

    public double getSellPrice(int amount) throws NoStorageException{
        double sum = 0;
        if(amount <0 ){
            System.out.println("Error: Amount to buy may not be negative!");
            throw new NumberFormatException("Amount to buy may not be negative!");
        }
        if(currentStock + amount > maxStock){
            System.out.println("Error: Not enough available!");
            throw new NoStorageException("The city does not have enough storage to complete the transaction!");
        }

        for(int i = 1; i<=amount; i++){ //start with one, as the NPC merchant buys at the price where they can sell it at
            sum += getPriceAtStock(getCurrentStock()+i);
        }
        return sum;
    }


    public String toString(){
        return name;
    }

    public void advanceDay(){
        currentStock += production;
        currentStock -= consumption;
        if(currentStock > maxStock){
            currentStock = maxStock;
        }
        else if(currentStock <0) {
            currentStock = 0;
        }
    }
}
