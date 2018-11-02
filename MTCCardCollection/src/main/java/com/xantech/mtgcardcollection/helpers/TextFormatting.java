package com.xantech.mtgcardcollection.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TextFormatting {
    public static String FormatAsPercentage(double value){
        String result = new DecimalFormat("##.##%").format(value);
        return result;

    }

    public static String FormatAsUSD(double value){
        String result = NumberFormat.getCurrencyInstance().format(value);
        return result;
    }
}
