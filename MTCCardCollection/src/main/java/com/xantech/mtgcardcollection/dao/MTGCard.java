package com.xantech.mtgcardcollection.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MTGCard extends AbstractEntity {
    private String block;
    private String card;
    private String format;
    private double twentyFourHourValueShift;
    private double twentyFourHourPercentageShift;
    private double sevenDayValueShift;
    private double sevenDayHourPercentageShift;  // TO DO: Refactor: Should be sevenDayPercentageShift
    private double thirtyDayValueShift;
    private double thirtyDayPercentageShift;
    private double allTimeValueShift;
    private double allTimePercentageShift;
    private String mtgGoldfishURL;
    private Date lastValueCheck;
    private double mostRecentValue;
    private String imageURL;

    @Override
    public String toString(){
        return card.replace('+', ' ') + " [" + block.replace('+',' ') + "]";
    }
}
