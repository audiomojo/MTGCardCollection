package com.xantech.mtgcardcollection.helpers;

public class MTGGoldfishURLParser {
    private final String url;

    public MTGGoldfishURLParser(String url) {
        this.url = url;
    }

    public static String GetBlock(String url) {
        String block = "";
        String[] parts = url.split("/");
        block = parts[4];
        return block;

    }
    public static String GetCard(String url) {
        String card = "";
        String[] parts = url.split("/");
        card = parts[5];
        parts = card.split("#");
        card = parts[0];
        return card;
    }

    public static String GetFormat(String url) {
        String format = "";
        String[] parts = url.split("#");
        format = parts[1];
        return format;
    }

}
