package com.xantech.mtgcardcollection.dto;

import lombok.Data;

@Data
public class MTGCardPurgeResponseDTO {
    private boolean overallPurgeActionSuccess;
    private boolean purgeCollectionAssetSuccess;
    private boolean purgeCardValueHistorySuccess;
    private int cardValueHistorySize;
    private boolean purgeCardSuccess;
    private String note;
}
