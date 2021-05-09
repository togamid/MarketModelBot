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

    //returns all Products that contain the given part of the name
    public Product[] searchProduct(String name){
        return  products.values().stream().filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())).toArray(Product[]::new);
    }

    public Product[] searchProductByCategory(String category){
        return  products.values().stream().filter(product -> product.getCategory().toLowerCase().contains(category.toLowerCase())).toArray(Product[]::new);
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
