package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CityMarket {
    private Product[] products;
    private String name;

    public CityMarket(Product[] products, String name){
        this.products = products;
        this.name = name;
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
                int production = Integer.parseInt(values[1]);
                int consumption = Integer.parseInt(values[2]);
                int maxStock = Integer.parseInt(values[3]);
                int minPrice = Integer.parseInt(values[4]);
                int currStock = Integer.parseInt(values[5]);
                products.add(new Product(name, production, consumption, maxStock, minPrice, currStock));

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
