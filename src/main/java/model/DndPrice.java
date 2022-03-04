package model;

import java.text.NumberFormat;
import java.util.Locale;

public class DndPrice {
    private final long copperAmount;
    private final int decimalPoints = 2;
    private final int GPRatio = (int) Math.pow(10, decimalPoints);
    private final int SPRatio = (int) Math.pow(10, decimalPoints-1);
    private final double CPRatio = (int) Math.pow(10, decimalPoints-2);

    public DndPrice(double price){
        copperAmount = (int) Math.ceil(price*100);
    }


    public static String getPrice(double price, boolean fractional){
        return new DndPrice(price).getPrice(fractional);
    }

    public String getPrice(boolean fractional){
        long gp = (copperAmount/GPRatio);
        long sp = (copperAmount%GPRatio)/SPRatio;
        double cp = (copperAmount%SPRatio) / CPRatio;
        if(!fractional){
            cp = Math.ceil(cp);
        }
        return ""+ NumberFormat.getInstance(Locale.US).format( gp)+" GP "+sp+" SP "+cp+" CP";
    }

    public String toString(){
        return getPrice(false);
    }
}
