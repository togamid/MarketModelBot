package model;



public class Product {
    public static final int numberProperties = 7;
    private final String name;
    private final int production; //how much is produced each day
    private final int consumption; //how much is consumed each day
    private final double minPrice; //the minimum price possible. Is approached asymptotically
    private final int priceVolatilityFactor; //this defines how much the price increases if the stock decreases
    private int currentStock;
    private final int maxStock; //the maximum amount the city can store of this good. Also used to scale the price-development to the expected quantity




    public Product(String name, int production, int consumption, int maxStock, int minPrice, int priceVolatilityFactor, int currentStock){
        this.name = name;
        this.production = production;
        this.consumption = consumption;
        this.maxStock = maxStock;
        this.minPrice = minPrice;
        this.priceVolatilityFactor = priceVolatilityFactor;
        this.currentStock = currentStock;
    }

    public String getName(){
        return name;
    }

    public int getCurrentStock(){
        return currentStock;
    }

    public boolean processTransaction(Transaction trans){
        int newStock = currentStock + trans.amount;
        if(newStock < 0 || newStock > maxStock) {
            return false;
        }
        else {
            currentStock = currentStock + trans.amount;
            return true;
        }

    }

    public void setCurrentStock(int newStock){
        if(newStock > maxStock){
            System.out.println("Error: NewStock " + newStock + " bigger than maxStock " + maxStock + " for Product "+ name);
        } else if(newStock < 0) {
            System.out.println("Error: NewStock " + newStock + " less than zero for Product "+ name);
        } else {
            currentStock = newStock;
        }
    }

    public double getPriceAtStock(int stock){

        double tmpConsum = 0.2;
        if(consumption != 0) {
            tmpConsum = consumption;
        }

        double tmpProduct = 0.2;
        if(production != 0) {
            tmpProduct = production;
        }

        double xAxisStretching = (tmpProduct/tmpConsum)* 16.0/(maxStock); //if there is more production than consumption, squash the graph on the x-Axis. Use maxStock to calibrate for the expected amount
        double yAxisStretching = priceVolatilityFactor; //scale the price increase with the priceDevelopmentFactor
        return (yAxisStretching/(stock*xAxisStretching + 0.5)) + minPrice;
    }


    public String toString(){
        return name;
    }
}
