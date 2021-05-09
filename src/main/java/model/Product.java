package model;


import frontend.Bot;
import model.exceptions.NoStorageException;
import model.exceptions.ProductNotAvailableException;
import org.jetbrains.annotations.NotNull;

public class Product implements Comparable{
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

    public String[] getInfoAsStringArray() {
        String[] response = new String[5];
        response[0] = this.name;
        response[1] = Integer.toString(this.getCurrentStock());
        response[2] = Integer.toString(this.getMaxStock());
        String buyPrice;
        try{
            buyPrice = DndPrice.getPrice(this.getBuyPrice(1), true);
        }
        catch (ProductNotAvailableException e){
            buyPrice = "N/A";
        }
        response[3] = buyPrice;
        String sellPrice;
        try{
            sellPrice = DndPrice.getPrice(this.getSellPrice(1), true);
        }
        catch (NoStorageException e){
            sellPrice = "N/A";
        }
        response[4] =  sellPrice;
        return response;
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

    @Override
    public int compareTo(@NotNull Object o) {
        if(o.getClass() == this.getClass()){
            Product other = (Product) o;

            if(this.consumption > 0 && other.consumption == 0 ){
                return -1;
            }
            else if(this.consumption == 0 && other.consumption >0){
                return 1;
            }
            else if(this.production > 0 && other.production == 0){
                return -1;
            }
            else if(this.production == 0 && other.production >0) {
                return 1;
            }
            else {
                return this.getName().compareTo(other.getName());
            }
        }
        return 0;
    }
}
