package model;



public class Product {
    public static final int numberProperties = 6;
    private String name;
    private int production;
    private int consumption;
    private double minPrice;
    private int currentStock;
    private int maxStock;



    public Product(String name, int production, int consumption, int maxStock, int minPrice, int currentStock){
        this.name = name;
        this.production = production;
        this.consumption = consumption;
        this.maxStock = maxStock;
        this.minPrice = minPrice;
        this.currentStock = currentStock;
    }

    public double getPrice(int number){
        return 0.0;
    }


    public String toString(){
        return name;
    }
}
