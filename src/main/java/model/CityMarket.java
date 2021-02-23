package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CityMarket {
    private final Product[] products;
    private final String name;
    private final ConcurrentHashMap<String, Product> productsMap = new ConcurrentHashMap<>();
    private final double NpcTraderMargin = 1.1;

    public CityMarket(Product[] products, String name){
        this.products = products;
        this.name = name;
        for (Product product: products ){
            productsMap.putIfAbsent(product.getName(), product);
        }
    }

    public String getName(){
        return name;
    }

    public String getProductList(){
        //TODO: make this better
        String list = "";
        for(int i = 0; i<products.length; i++){
            list += products[i].toString();
            list += "\n";
        }
        return list;
    }

    public Product getProduct(String name){
        return productsMap.get(name);
    }


    public double getCurrentPriceOfProduct(String productName){
        Product product = productsMap.get(productName);
        return product.getPriceAtStock(product.getCurrentStock());
    }


    public double getSellPrice(String productName, int amount){
        Product product = productsMap.get(productName);
        double sum = 0;
        if(amount <0 ){
            System.out.println("Error: Amount to Sell may not be negative!");
            return 0.0;
        }

        for(int i = 1; i<=amount; i++){ //start with one, as the NPC merchant buys at the price where they can sell it at
            sum += product.getPriceAtStock(product.getCurrentStock()+i);
        }
        return sum;

    }
    ///returns the amount one has to pay to buy the specified amount of the specified good.
    ///if there is an Error, it returns 0.0

    /**
     *
     * @param productName the name of the product to buy
     * @param amount the amount to buy
     * @return the price for the transaction or 0.0 if there is an error
     */
    public double getBuyPrice(String productName, int amount){
        Product product = productsMap.get(productName);
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


    public static CityMarket loadCityMarket(String path, boolean ignoreFirstLine){
        List<Product> products = new LinkedList<>();
        FileReader fileReader;
        try{
            fileReader = new FileReader(path);
        } catch(Exception e){
            System.out.println("Path not correct: " + path);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String line = reader.readLine();
            if(ignoreFirstLine)
                line = reader.readLine();
             while(line != null){
                String[] values = line.split(";");

                if(values.length != Product.numberProperties){
                    System.out.println("Malformed CSV; not the right amount of properties: " + line);
                    return null;
                }

                String name = values[0];
                int production = Integer.parseInt(values[1].trim());
                int consumption = Integer.parseInt(values[2].trim());
                int maxStock = Integer.parseInt(values[3].trim());
                int minPrice = Integer.parseInt(values[4].trim());
                int priceVolatility = Integer.parseInt(values[5].trim());
                int currStock = Integer.parseInt(values[6].trim());
                products.add(new Product(name, production, consumption, maxStock, minPrice, priceVolatility,currStock));

                line = reader.readLine();
             }

             return new CityMarket(products.toArray(new Product[0]), "test");
        } catch(IOException e) {
            System.out.println("IOException");
            return null;
        } catch (NumberFormatException e){
            System.out.println("Malormed CSV: " + path);
            return null;
        }


    }
}
