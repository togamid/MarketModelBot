package data;

import frontend.Bot;
import model.CityMarket;
import model.Model;
import model.Product;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DataConnector {
    private final String path;
    private final String[] cities;

    public DataConnector(String path){
        this.path = path;
        File directory = new File(path);
        if(!directory.exists() && directory.isDirectory()){
            System.out.println("[Error] The specified directory ("+ directory.getAbsolutePath() + ") does not exist!");
            Bot.shutdown();
        }
        this.cities = directory.list((f,s) -> (s.endsWith(".txt") ));
        if( cities.length == 0){
            System.out.println("[Warning] No city CSVs could be found in the directory " + directory.getAbsolutePath());
        }
    }
    public Model loadModel(){
        List<CityMarket> cityObjects = new LinkedList<>();
        for(String city : cities){
            CityMarket market = loadCityMarket(path + File.separator + city, city.substring(0,city.indexOf(".")), true);
            if(market != null){
                cityObjects.add(market);
            } else {
                System.out.println("An error occured with the market "+ city+". Skipped it!");
            }

        }
        return new Model(cityObjects.toArray(new CityMarket[0]));
    }

    private CityMarket loadCityMarket(String path, String cityName, boolean ignoreFirstLine){
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
                String category = values[1];
                double production = Double.parseDouble(values[2].trim());
                double consumption = Double.parseDouble(values[3].trim());
                int maxStock = (int) Double.parseDouble(values[4].trim());
                double minPrice = Double.parseDouble(values[5].trim());
                double maxPrice = Double.parseDouble(values[6].trim());
                double currStock = Double.parseDouble(values[7].trim());
                products.add(new Product(name,category, production, consumption, maxStock, minPrice, maxPrice,currStock));

                line = reader.readLine();
            }

            Product[] productsArray = products.toArray(new Product[0]);

            return new CityMarket(productsArray, cityName);
        } catch(IOException e) {
            System.out.println("IOException");
            return null;
        } catch (NumberFormatException e){
            System.out.println("Malformed CSV: " + path);
            return null;
        }
    }

    public boolean safeModel(Model model){
        boolean noError = true;
        for(CityMarket market : model.getAllMarkets()){
            String firstLine="name;production;consumption;maxStock;minPrice;maxPrice;currentStock";
            noError = noError & safeCityMarket(market, path + File.separator + market.getName() + ".txt", firstLine);
        }
        return noError;
    }
    private boolean safeCityMarket(CityMarket market, String path, String firstLine){
        StringBuilder builder = new StringBuilder(firstLine);
        for (Product product: market.getAllProducts()) {
            builder.append("\n");
            builder.append(product.name);
            builder.append(";");
            builder.append(product.category);
            builder.append(";");
            builder.append(product.production);
            builder.append(";");
            builder.append(product.consumption);
            builder.append(";");
            builder.append(product.getMaxStock());
            builder.append(";");
            builder.append(product.minPrice);
            builder.append(";");
            builder.append(product.maxPrice);
            builder.append(";");
            builder.append(product.getExactCurrentStock());
        }
        FileWriter fileWriter;
        try{
            fileWriter = new FileWriter(path);
        } catch(IOException e){
            System.out.println("Path not correct: " + path);
            return false;
        }
        try{
            fileWriter.write(builder.toString());
        }catch (IOException e){
            System.out.println("An error occurred while saving!");
            return false;
        }
        try{
            fileWriter.close();
        } catch(IOException e){
            System.out.println("An error occurred after saving!");
        }
        return true;
    }
}
