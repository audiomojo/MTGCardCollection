package com.xantech.mtgcardcollection.helpers;

import com.xantech.mtgcardcollection.dao.MTGCard;
import org.springframework.stereotype.Component;


@Component
public class MTGGoldFishCardValueEngine {
    public double getCardValue(MTGCard mtgCard) {
        ScreenScrapeCardValueMTGGoldfish screenScrapeCardValueMTGGoldfish = new ScreenScrapeCardValueMTGGoldfish();
        double value = Double.parseDouble(screenScrapeCardValueMTGGoldfish.getPrice(mtgCard.getMtgGoldfishURL(), mtgCard.getFormat()));
        return value;
    }

    public String getImageURL(MTGCard mtgCard) {
        ScreenScrapeCardValueMTGGoldfish screenScrapeCardValueMTGGoldfish = new ScreenScrapeCardValueMTGGoldfish();
        String imageURL = screenScrapeCardValueMTGGoldfish.getImageURL(mtgCard.getMtgGoldfishURL());
        return imageURL;
    }
}
