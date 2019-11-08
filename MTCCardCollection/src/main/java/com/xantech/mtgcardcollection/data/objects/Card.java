package com.xantech.mtgcardcollection.data.objects;

import com.xantech.mtgcardcollection.data.collections.ValueHistory;
import lombok.Data;

import java.io.Serializable;

@Data
public class Card implements Serializable {
    //private static final long serialVersionUID = 3519903057733752236L;
    private static final long serialVersionUID = 726528664611708250L;
    private int id;
    private String block;
    private String card;
    private String format;
    private int quantity;
    private ValueHistory valueHistory;
    private CardValueMetrics cardValueMetrics;

    public Card(int id, String block, String card, String format, int quantity)
    {
        this.id = id;
        this.block = block;
        this.card = card;
        this.format = format;
        this.quantity = quantity;
        this.valueHistory = new ValueHistory();
        this.cardValueMetrics = new CardValueMetrics();
    }

//    public Card Clone() {
//        Card clone = new Card(id, block, card, format, quantity);
//        return clone;
//    }

//    public double MeasureValue() {
//        //ScreenScrapeCardValue screenScrapeCardValue = new ScreenScrapeCardValue(block, card, format);
//        //double value = Double.parseDouble(screenScrapeCardValue.getPrice());
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
//        return value;
//    }

//    public double GetCardValue() {
//        double cardValue;
//
//        ValueNode valueNode = valueHistory.GetTodaysValue();
//        if (valueNode != null)
//            cardValue = valueNode.getValue();
//        else
//            cardValue = MeasureValue();
//
//        return cardValue;
//    }
}
