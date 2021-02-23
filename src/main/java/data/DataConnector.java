package data;

import model.CityMarket;
import model.Model;
import model.Product;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DataConnector {
    private final String path;
    private final String[] cities;

    public DataConnector(String path, String[] cities){
        this.path = path;
        this.cities = cities;
    }
    public Model loadModel(){
        List<CityMarket> cityObjects = new LinkedList<>();
        for(String city : cities){
           cityObjects.add(loadCityMarket(path + File.separator + city + ".txt", city, true));
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
                int production = Integer.parseInt(values[1].trim());
                int consumption = Integer.parseInt(values[2].trim());
                int maxStock = Integer.parseInt(values[3].trim());
                double minPrice = Double.parseDouble(values[4].trim());
                int priceVolatility = Integer.parseInt(values[5].trim());
                int currStock = Integer.parseInt(values[6].trim());
                products.add(new Product(name, production, consumption, maxStock, minPrice, priceVolatility,currStock));

                line = reader.readLine();
            }

            return new CityMarket(products.toArray(new Product[0]), cityName);
        } catch(IOException e) {
            System.out.println("IOException");
            return null;
        } catch (NumberFormatException e){
            System.out.println("Malormed CSV: " + path);
            return null;
        }
    }

    public boolean safeModel(Model model){
        boolean noError = true;
        for(CityMarket market : model.getAllMarkets()){
            String firstLine="name;production;consumption;maxStock;minPrice;volatility,currentStock";
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
            builder.append(product.production);
            builder.append(";");
            builder.append(product.consumption);
            builder.append(";");
            builder.append(product.getMaxStock());
            builder.append(";");
            builder.append(product.minPrice);
            builder.append(";");
            builder.append(product.priceVolatilityFactor);
            builder.append(";");
            builder.append(product.getCurrentStock());
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
            System.out.println("An error occured while saving!");
            return false;
        }
        try{
            fileWriter.close();
        } catch(IOException e){
            System.out.println("An error occured after saving!");
        }
        return true;
    }
}
