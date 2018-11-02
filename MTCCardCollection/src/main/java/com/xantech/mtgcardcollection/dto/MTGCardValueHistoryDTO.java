package com.xantech.mtgcardcollection.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MTGCardValueHistoryDTO {
    private Date date;
    private String dateString;
    private double value;
}
