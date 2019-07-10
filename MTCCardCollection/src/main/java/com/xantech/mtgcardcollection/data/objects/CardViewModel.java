package com.xantech.mtgcardcollection.data.objects;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCollectionAsset;
import com.xantech.mtgcardcollection.dao.MTGDeckAsset;
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
    private String url;
    private String imageUrl;

    public CardViewModel() {}

    public CardViewModel(MTGCollectionAsset mtgCollectionAsset, MTGCard mtgCard){
        this.setBlock(mtgCard.getBlock());
        this.setCard(mtgCard.getCard());
        this.setFormat(mtgCard.getFormat());
        this.setQuantity(Integer.toString(mtgCollectionAsset.getQuantity()));
        this.setValue(mtgCard.getMostRecentValue());
        this.setValueStr(TextFormatting.FormatAsUSD(mtgCard.getMostRecentValue()));
        this.setTotalValue(mtgCard.getMostRecentValue() * mtgCollectionAsset.getQuantity());
        this.setTotalValueStr(TextFormatting.FormatAsUSD(mtgCard.getMostRecentValue() * mtgCollectionAsset.getQuantity()));
        this.setTwentyFourHourValueShift(mtgCard.getTwentyFourHourValueShift());
        this.setTwentyFourHourValueShiftStr(TextFormatting.FormatAsUSD(mtgCard.getTwentyFourHourValueShift()));
        this.setTwentyFourHourPercentageShift(mtgCard.getTwentyFourHourPercentageShift());
        this.setTwentyFourHourPercentageShiftStr(TextFormatting.FormatAsPercentage(mtgCard.getTwentyFourHourPercentageShift()));
        this.setSevenDayValueShift(mtgCard.getSevenDayValueShift());
        this.setSevenDayValueShiftStr(TextFormatting.FormatAsUSD(mtgCard.getSevenDayValueShift()));
        this.setSevenDayPercentageShift(mtgCard.getSevenDayHourPercentageShift());
        this.setSevenDayPercentageShiftStr(TextFormatting.FormatAsPercentage(mtgCard.getSevenDayHourPercentageShift()));
        this.setThirtyDayValueShift(mtgCard.getThirtyDayValueShift());
        this.setThirtyDayValueShiftStr(TextFormatting.FormatAsUSD(mtgCard.getThirtyDayValueShift()));
        this.setThirtyDayPercentageShift(mtgCard.getThirtyDayPercentageShift());
        this.setThirtyDayPercentageShiftStr(TextFormatting.FormatAsPercentage(mtgCard.getThirtyDayPercentageShift()));
        this.setAllTimeValueShift(mtgCard.getAllTimeValueShift());
        this.setAllTimeValueShiftStr(TextFormatting.FormatAsUSD(mtgCard.getAllTimeValueShift()));
        this.setAllTimePercentageShift(mtgCard.getAllTimePercentageShift());
        this.setAllTimePercentageShiftStr(TextFormatting.FormatAsPercentage(mtgCard.getAllTimePercentageShift()));
        this.setUrl(mtgCard.getMtgGoldfishURL());
        this.setImageUrl(mtgCard.getImageURL());
    }

    public String toString() {
        return block + " - " + card + " - " + format + " - " + quantity + " - " + valueStr + " - " + totalValueStr + " - " + twentyFourHourValueShiftStr;
    }

}
