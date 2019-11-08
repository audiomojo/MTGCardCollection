package com.xantech.mtgcardcollection.dto;

import com.xantech.mtgcardcollection.dao.MTGCard;
import lombok.Data;

import java.util.List;

@Data
public class GoldfishTCGUrlConversionDTO {
    private int collectionCardCount;
    private int conversionFailueCount;
    private int conversionSuccessCount;
    private List<MTGCard> conversionFailures;
}
