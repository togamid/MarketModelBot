package frontend;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Config {

    public final HashMap<String, String> config = new HashMap<>();
    private String[] cities;

    public Config(String path){
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String entry = myReader.nextLine();
                String key = entry.substring(0,entry.indexOf(':')).trim();
                String value = entry.substring(entry.indexOf(':')+1).trim();
                config.put(key,value);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The config file could not be loaded. Did your remember to create it?");
            e.printStackTrace();
        }

        if(config.get("Cities") != null){
            String[] cities = config.get("Cities").split(",");
            for (int i = 0; i< cities.length; i++) {
                cities[i] = cities[i].trim();
            }
            this.cities = cities;
        }
    }
    public String get(String key){
        return config.get(key);
    }

    public String[] getCities() {
        return cities;
    }
}