package model;

import java.util.HashMap;

public class Model {
    private final HashMap<String, CityMarket > cities = new HashMap<>();

    public Model(CityMarket[] citiesList){
        for (CityMarket city :citiesList) {
            cities.put(city.getName(), city);
        }
    }

    public CityMarket getMarket(String name){
        return cities.get(name);
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
