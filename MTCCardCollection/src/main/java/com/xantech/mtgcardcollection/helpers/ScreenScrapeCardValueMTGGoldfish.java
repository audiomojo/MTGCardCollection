package com.xantech.mtgcardcollection.helpers;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

@Slf4j
public class ScreenScrapeCardValueMTGGoldfish implements ScreenScrapeCardValue {

    @Override
    public String getPrice(String url, String format) {
        Document document;
        String price = "0.0";

        try {
            //System.out.println("Fetching price for: " + url);
            document = Jsoup.connect(url).get();
            //System.out.println("Document Received: " + document.outerHtml());
            Elements priceDiv;
            if (document.outerHtml().contains("Throttled")) {
                System.out.print("ERROR -- Throttled: ");
            } else {

                if (format.compareTo("paper") == 0)
                    priceDiv = document.body().getElementsByClass("price-box paper");
                else
                    priceDiv = document.body().getElementsByClass("price-box online");

                if (priceDiv != null && priceDiv.size() > 0) {

                    for (Element element : priceDiv.first().children()) {
                        if (element.className().compareTo("price-box-price") == 0)
                            price = element.text().substring(2);
                    }
                } else {
                    price = "-1.0"; // MTGGoldFish had a glitch and did not have the proper price div.  Setting to -1 to prevent infinite "Throttled" loop.
                }
            }
        } catch (IOException e) {
            System.out.println("Error Getting Value for: " + url);
        }

        return price;
    }

    public String getImageURL(String url) {
        Document document;
        String imageURL = "tbd";

        try {
            //System.out.println("Fetching price for: " + assembledURL);
            document = Jsoup.connect(url).get();
            //System.out.println("Document Received: " + document.outerHtml());
            Elements imageDiv;
            if (document.outerHtml().contains("Throttled")) {
                System.out.print("ERROR -- Throttled: ");
            } else {
                imageDiv = document.body().getElementsByClass("price-card-image-image");

                if (imageDiv != null) {

                    imageURL = imageDiv.first().attr("src");
                }
            }
        } catch (IOException e) {
            System.out.println("Error Getting Image URL for: " + url);
        }

        return imageURL;
    }
}
