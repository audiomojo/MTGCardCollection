package com.xantech.mtgcardcollection.data.objects;

import lombok.Data;

import java.io.Serializable;

@Data
public class CardValueMetrics implements Serializable {

    private static final long serialVersionUID = -7909633934942089803L;
    private double twentyFourHourValueShift;
    private double twentyFourHourPercentageShift;
    private double sevenDayValueShift;
    private double sevenDayHourPercentageShift;
    private double thirtyDayValueShift;
    private double thirtyDayPercentageShift;
    private double allTimeValueShift;
    private double allTimePercentageShift;
}
