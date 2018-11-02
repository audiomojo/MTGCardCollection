package com.xantech.mtgcardcollection.helpers;

import com.xantech.mtgcardcollection.dao.MTGCard;
import org.springframework.stereotype.Component;


@Component
public class MTGGoldFishCardValueEngine {
    public double getCardValue(MTGCard mtgCard) {
        ScreenScrapeCardValue screenScrapeCardValue = new ScreenScrapeCardValue();
        double value = Double.parseDouble(screenScrapeCardValue.getPrice(mtgCard.getMtgGoldfishURL(), mtgCard.getFormat()));
        return value;
    }

    public String getImageURL(MTGCard mtgCard) {
        ScreenScrapeCardValue screenScrapeCardValue = new ScreenScrapeCardValue();
        String imageURL = screenScrapeCardValue.getImageURL(mtgCard.getMtgGoldfishURL());
        return imageURL;
    }
}
