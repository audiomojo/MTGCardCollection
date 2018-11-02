package com.xantech.mtgcardcollection.dto;

import lombok.Data;

import java.util.List;

@Data
public class MTGDeckListDTO {
    private String deckName;
    private double deckValue;
    private String deckValueStr;
    private List<MTGDeckAssetDTO> mtgDeckAssetDTOList;
}
