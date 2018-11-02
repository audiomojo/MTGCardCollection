package com.xantech.mtgcardcollection.dto;

import lombok.Data;

@Data
public class MTGDeckAssetDTO {
    private String block;
    private String card;
    private int quantity;
    private double value;
}
