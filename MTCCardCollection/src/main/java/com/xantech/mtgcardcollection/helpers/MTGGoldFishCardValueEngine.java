package com.xantech.mtgcardcollection.helpers;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.data.collections.ValueHistory;
import com.xantech.mtgcardcollection.data.objects.CardValueMetrics;
import com.xantech.mtgcardcollection.data.objects.ValueNode;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MTGGoldFishCardValueEngine {
    public double getCardValue(MTGCard mtgCard) {
        double cardValue = 0;

        ScreenScrapeCardValue screenScrapeCardValue = new ScreenScrapeCardValue(mtgCard.getBlock(), mtgCard.getCard(), mtgCard.getFormat());
        double value = Double.parseDouble(screenScrapeCardValue.getPrice());
//        if (valueHistory == null)
//            valueHistory = new ValueHistory();
//        valueHistory.AddNode(new ValueNode(new Date(), value));
//
//        if(cardValueMetrics == null)
//            cardValueMetrics = new CardValueMetrics();
//        cardValueMetrics.setTwentyFourHourValueShift(valueHistory.Get24HourValueShift());
//        cardValueMetrics.setTwentyFourHourPercentageShift(valueHistory.Get24HourPercentageShift());
//        cardValueMetrics.setSevenDayValueShift(valueHistory.Get7DayValueShift());
//        cardValueMetrics.setSevenDayHourPercentageShift(valueHistory.Get7DayPercentageShift());
//        cardValueMetrics.setThirtyDayValueShift(valueHistory.Get30DayValueShift());
//        cardValueMetrics.setThirtyDayPercentageShift(valueHistory.Get30DayPercentageShift());
//        cardValueMetrics.setAllTimeValueShift(valueHistory.GetAllTimeValueShift());
//        cardValueMetrics.setAllTimePercentageShift(valueHistory.GetAllTimePercentageShift());
        return value;
    }
}
