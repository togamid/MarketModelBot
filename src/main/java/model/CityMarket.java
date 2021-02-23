package model;

import java.util.concurrent.ConcurrentHashMap;

public class CityMarket {
    private final String name;
    private final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();
    private final double NpcTraderMargin = 1.1;

    public CityMarket(Product[] products, String name){
        this.name = name;
        for (Product product: products ){
            this.products.putIfAbsent(product.getName(), product);
        }
    }

    public String getName(){
        return name;
    }


    public Product getProduct(String name){
        return products.get(name);
    }

    public Product[] getAllProducts(){
        return products.values().toArray(new Product[0]);
    }

    public double getCurrentPriceOfProduct(String productName){
        Product product = products.get(productName);
        return product.getPriceAtStock(product.getCurrentStock());
    }


    public double getSellPrice(String productName, int amount){
        Product product = products.get(productName);
        if(product == null){
            System.out.println("Error: Product "+ productName + "not found in Market "+ this.name);
            return 0.0;
        }
        double sum = 0;
        if(amount <0 ){
            System.out.println("Error: Amount to Sell may not be negative!");
            return 0.0;
        }
        if(product.getCurrentStock() + amount > product.getMaxStock()){
            System.out.println("Error: Not enough storage space available!");
            return 0.0;
        }

        for(int i = 1; i<=amount; i++){ //start with one, as the NPC merchant buys at the price where they can sell it at
            sum += product.getPriceAtStock(product.getCurrentStock()+i);
        }
        return sum;

    }
    /**
     *
     * @param productName the name of the product to buy
     * @param amount the amount to buy
     * @return the price for the transaction or 0.0 if there is an error
     */
    public double getBuyPrice(String productName, int amount){
        Product product = products.get(productName);
        if(product == null){
            System.out.println("Error: Product "+ productName + "not found in Market "+ this.name);
            return 0.0;
        }
        double sum = 0;
        if(amount <0 ){
            System.out.println("Error: Amount to buy may not be negative!");
            return 0.0;
        }
        if(product.getCurrentStock() - amount < 0){
            System.out.println("Error: Not enough available!");
            return 0.0;
        }
        for(int i = 0; i<amount; i++){
            sum += product.getPriceAtStock(product.getCurrentStock()-i);
        }
        return sum*NpcTraderMargin;

    }

}
