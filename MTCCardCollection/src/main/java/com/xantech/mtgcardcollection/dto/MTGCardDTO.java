package com.xantech.mtgcardcollection.dto;

import lombok.Data;

import java.util.List;

@Data
public class MTGCardDTO {
    private String block;
    private String card;
    private String format;
    private int quantity;
    private double purchasePrice;
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
}
