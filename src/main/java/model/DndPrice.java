package model;

public class DndPrice {
    private final int copperAmount;

    public DndPrice(double price){
        copperAmount = (int) Math.round(price*100);
    }

    public DndPrice(int price){
        copperAmount = price;
    }

    public static String getPrice(double price){
        return new DndPrice(price).getPrice();
    }

    public String getPrice(){
        int gp = (copperAmount/100);
        int sp = (copperAmount%100)/10;
        int cp = (copperAmount%10);
        return ""+gp+" GP "+sp+" SP "+cp+" CP";
    }

    public String toString(){
        return getPrice();
    }
}
