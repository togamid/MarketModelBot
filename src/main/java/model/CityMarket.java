package model;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class CityMarket {
    private final String name;
    private final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    public CityMarket(Product[] products, String name){
        this.name = name;
        for (Product product: products ){
            this.products.putIfAbsent(product.getName().toLowerCase(Locale.ROOT), product);
        }
    }

    public String getName(){
        return name;
    }


    public Product getProduct(String name){
        return products.get(name.toLowerCase(Locale.ROOT));
    }

    public Product[] getAllProducts(){
        return products.values().toArray(new Product[0]);
    }

    public void advanceDay(){
        for (Product product: getAllProducts()) {
            product.advanceDay();
        }
    }

}
