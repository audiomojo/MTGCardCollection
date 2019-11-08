package com.xantech.mtgcardcollection.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScreenScrapeCardValueTCGPlayer implements ScreenScrapeCardValue{

    @Override
    public String getPrice(String url, String format) {
        Document document;
        String price = "0.0";

        try {
            //System.out.println("Fetching price for: " + url);
            document = Jsoup.connect(url).get();
            //System.out.println("Document Received: " + document.outerHtml());
            Elements priceDiv = document.body().getElementsByClass("price-point price-point--market");
            if (priceDiv != null && priceDiv.size() > 0) {

                for (Element element : priceDiv.first().children()) {
                    if (element.className().compareTo("price-box-price") == 0)
                        price = element.text();
                }
            } else {
                price = "-1.0"; // MTGGoldFish had a glitch and did not have the proper price div.  Setting to -1 to prevent infinite "Throttled" loop.
            }
        } catch (IOException e) {
            System.out.println("Error Getting Value for: " + url);
        }

        return price;
    }
}
