package com.xantech.mtgcardcollection.data.objects;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCollectionAsset;
import com.xantech.mtgcardcollection.helpers.TextFormatting;
import lombok.Data;

@Data
public class CardViewModel {
    private String block;
    private String card;
    private String format;
    private String quantity;
    private double value;
    private String valueStr;
    private double totalValue;
    private String totalValueStr;
    private double twentyFourHourValueShift;
    private String twentyFourHourValueShiftStr;
    private double twentyFourHourPercentageShift;
    private String twentyFourHourPercentageShiftStr;
    private double sevenDayValueShift;
    private String sevenDayValueShiftStr;
    private double sevenDayPercentageShift;
    private String sevenDayPercentageShiftStr;
    private double thirtyDayValueShift;
    private String thirtyDayValueShiftStr;
    private double thirtyDayPercentageShift;
    private String thirtyDayPercentageShiftStr;
    private double allTimeValueShift;
    private String allTimeValueShiftStr;
    private double allTimePercentageShift;
    private String allTimePercentageShiftStr;

    public CardViewModel() {}

    public CardViewModel(MTGCollectionAsset mtgCollectionAsset, MTGCard mtgCard){
        this.block = mtgCard.getBlock();
        this.card = mtgCard.getCard();
        this.format = mtgCard.getFormat();
        this.quantity = Integer.toString(mtgCollectionAsset.getQuantity());
        this.value = mtgCard.getMostRecentValue();
        this.valueStr = TextFormatting.FormatAsUSD(mtgCard.getMostRecentValue());
        this.totalValue = mtgCard.getMostRecentValue() * mtgCollectionAsset.getQuantity();
        this.totalValueStr = TextFormatting.FormatAsUSD(mtgCard.getMostRecentValue() * mtgCollectionAsset.getQuantity());
        this.twentyFourHourValueShift = mtgCard.getTwentyFourHourValueShift();
        this.twentyFourHourValueShiftStr = TextFormatting.FormatAsUSD(mtgCard.getTwentyFourHourValueShift());
        this.twentyFourHourPercentageShift = mtgCard.getTwentyFourHourPercentageShift();
        this.twentyFourHourPercentageShiftStr = TextFormatting.FormatAsPercentage(mtgCard.getTwentyFourHourPercentageShift());
        this.sevenDayValueShift = mtgCard.getSevenDayValueShift();
        this.sevenDayValueShiftStr = TextFormatting.FormatAsUSD(mtgCard.getSevenDayValueShift());
        this.sevenDayPercentageShift = mtgCard.getSevenDayHourPercentageShift();
        this.sevenDayPercentageShiftStr = TextFormatting.FormatAsPercentage(mtgCard.getSevenDayHourPercentageShift());
        this.thirtyDayValueShift = mtgCard.getThirtyDayValueShift();
        this.thirtyDayValueShiftStr = TextFormatting.FormatAsUSD(mtgCard.getThirtyDayValueShift());
        this.thirtyDayPercentageShift = mtgCard.getThirtyDayPercentageShift();
        this.thirtyDayPercentageShiftStr = TextFormatting.FormatAsPercentage(mtgCard.getThirtyDayPercentageShift());
        this.allTimeValueShift = mtgCard.getAllTimeValueShift();
        this.allTimeValueShiftStr = TextFormatting.FormatAsUSD(mtgCard.getAllTimeValueShift());
        this.allTimePercentageShift = mtgCard.getAllTimePercentageShift();
        this.allTimePercentageShiftStr = TextFormatting.FormatAsPercentage(mtgCard.getAllTimePercentageShift());
    }
}
