package com.xantech.mtgcardcollection.dto;

import com.xantech.mtgcardcollection.dao.MTGDeck;
import lombok.Data;

import java.util.List;

@Data
public class MTGCardDTO {
    private String block;
    private String card;
    private String format;
    private int quantity;
    private double currentValue;
    private double twentyFourHourValueShift;
    private double twentyFourHourPercentageShift;
    private double sevenDayValueShift;
    private double sevenDayHourPercentageShift;
    private double thirtyDayValueShift;
    private double thirtyDayPercentageShift;
    private double allTimeValueShift;
    private double allTimePercentageShift;
    private String mtgGoldfishURL;
    private String notes;
    List<MTGCardValueHistoryDTO> valueHistoryDTOList;
    List<MTGDeckDTO> deckDTOList;
}
