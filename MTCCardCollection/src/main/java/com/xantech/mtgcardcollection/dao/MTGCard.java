package com.xantech.mtgcardcollection.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MTGCard extends AbstractEntity {
    private String block;
    private String card;
    private String format;
    private int quantity;
    private double twentyFourHourValueShift;
    private double twentyFourHourPercentageShift;
    private double sevenDayValueShift;
    private double sevenDayHourPercentageShift;  // TO DO: Refactor: Should be sevenDayPercentageShift
    private double thirtyDayValueShift;
    private double thirtyDayPercentageShift;
    private double allTimeValueShift;
    private double allTimePercentageShift;
    private String mtgGoldfishURL;
    @Lob
    private String notes;
    @Lob
    private String transactionHistory;
}
