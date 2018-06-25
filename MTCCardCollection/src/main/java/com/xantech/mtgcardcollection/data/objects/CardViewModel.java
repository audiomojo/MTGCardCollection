package com.xantech.mtgcardcollection.data.objects;

import com.xantech.mtgcardcollection.helpers.TextFormatting;
import lombok.Data;

@Data
public class CardViewModel {
    private String block;
    private String card;
    private String format;
    private String quantity;
    private String value;
    private String totalValue;
    private String twentyFourHourValueShift;
    private String twentyFourHourPercentageShift;
    private String sevenDayValueShift;
    private String sevenDayPercentageShift;
    private String thirtyDayValueShift;
    private String thirtyDayPercentageShift;
    private String allTimeValueShift;
    private String allTimePercentageShift;

    public CardViewModel() {}

    public CardViewModel(Card card){
        this.block = card.getBlock();
        this.card = card.getCard();
        this.format = card.getFormat();
        this.quantity = Integer.toString(card.getQuantity());
        this.value = TextFormatting.FormatAsUSD(card.GetCardValue());
        this.totalValue = TextFormatting.FormatAsUSD(card.GetCardValue() * card.getQuantity());
        this.twentyFourHourValueShift = TextFormatting.FormatAsUSD(card.getCardValueMetrics().getTwentyFourHourValueShift());
        this.twentyFourHourPercentageShift = TextFormatting.FormatAsPercentage(card.getCardValueMetrics().getTwentyFourHourPercentageShift());
        this.sevenDayValueShift = TextFormatting.FormatAsUSD(card.getCardValueMetrics().getSevenDayValueShift());
        this.sevenDayPercentageShift = TextFormatting.FormatAsPercentage(card.getCardValueMetrics().getSevenDayHourPercentageShift());
        this.thirtyDayValueShift = TextFormatting.FormatAsUSD(card.getCardValueMetrics().getThirtyDayValueShift());
        this.thirtyDayPercentageShift = TextFormatting.FormatAsPercentage(card.getCardValueMetrics().getThirtyDayPercentageShift());
        this.allTimeValueShift = TextFormatting.FormatAsUSD(card.getCardValueMetrics().getAllTimeValueShift());
        this.allTimePercentageShift = TextFormatting.FormatAsPercentage(card.getCardValueMetrics().getAllTimePercentageShift());
    }

}
