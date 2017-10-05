package com.xantech.mtgcardcollection.helpers;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

@Slf4j
public class ScreenScrapeCardValue {
    private final String block;
    private final String card;
    private final String format;
    private final String assembledURL;


    public ScreenScrapeCardValue(String block, String card, String format) {
        this.block = block;
        this.card = card;
        this.format = format;
        this.assembledURL = "https://www.mtggoldfish.com/price/".concat(this.block).concat("/").concat(this.card).concat("#").concat(this.format);
    }

    public String getPrice() { return scrapePrice(); }

    public String scrapePrice() {
        Document document;
        String price = "0.0";

        try {
            System.out.println("Fetching Price: " + assembledURL);
            document = Jsoup.connect(assembledURL).get();
            Elements priceDiv;

            if (format.compareTo("paper") == 0)
                priceDiv = document.body().getElementsByClass("price-box paper");
            else
                priceDiv = document.body().getElementsByClass("price-box online");

            for (Element element : priceDiv.first().children()) {
                if (element.className().compareTo("price-box-price") == 0)
                    price = element.text();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return price;
    }
}
