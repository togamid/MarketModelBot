package model;

import java.util.HashMap;
import java.util.Locale;

public class Model {
    private final HashMap<String, CityMarket > cities = new HashMap<>();

    public Model(CityMarket[] citiesList){
        for (CityMarket city :citiesList) {
            cities.put(city.getName().toLowerCase(Locale.ROOT), city);
        }
    }

    public CityMarket getMarket(String name){
        return cities.get(name.toLowerCase(Locale.ROOT));
    }

    public CityMarket[] getAllMarkets(){
        return cities.values().toArray(new CityMarket[0]);
    }

    public void advanceDay(){
        for (CityMarket market: getAllMarkets()) {
            market.advanceDay();
        }
    }

}
