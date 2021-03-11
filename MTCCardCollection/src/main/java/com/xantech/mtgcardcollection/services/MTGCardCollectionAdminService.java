package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.dto.MTGCardPurgeResponseDTO;
import com.xantech.mtgcardcollection.dto.MTGGoldfishURLUpdateDTO;
import com.xantech.mtgcardcollection.factories.GoldfishTCGUrlConversionDTOFactory;
import com.xantech.mtgcardcollection.helpers.ScreenScrapeCardValueTCGPlayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGCardCollectionAdminService {
    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    @Autowired
    MTGCollectionAssetRepository mtgCollectionAssetRepository;

    @Autowired
    GoldfishTCGUrlConversionDTOFactory goldfishTCGUrlConversionDTOFactory;

    @Autowired
    ScreenScrapeCardValueTCGPlayer screenScrapeCardValueTCGPlayer;

    @Autowired
    MTGUserService mtgUserService;

    public GoldfishTCGUrlConversionDTO generateAndValidateTCGURLs() {
        GoldfishTCGUrlConversionDTO goldfishTCGUrlConversionDTO = goldfishTCGUrlConversionDTOFactory.create();
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        goldfishTCGUrlConversionDTO.setCollectionCardCount(mtgCardList.size());
        goldfishTCGUrlConversionDTO.setConversionSuccessCount((int)mtgCardList.stream().filter(mtgCard -> convertAndValidate(mtgCard, goldfishTCGUrlConversionDTO) == true).count());
        goldfishTCGUrlConversionDTO.setConversionFailueCount(goldfishTCGUrlConversionDTO.getCollectionCardCount()-goldfishTCGUrlConversionDTO.getConversionSuccessCount());
        return goldfishTCGUrlConversionDTO;
    }

    private boolean convertAndValidate(MTGCard mtgCard, GoldfishTCGUrlConversionDTO goldfishTCGUrlConversionDTO) {
        boolean result = true;

        String url = buildTCGPlayerURL(mtgCard);

        String price = screenScrapeCardValueTCGPlayer.getPrice(url, "");

        if (price.compareTo("0.0") == 0){
            result = false;
        }

        return result;
    }

    private String buildTCGPlayerURL(MTGCard mtgCard) {
        String url = "https://shop.tcgplayer.com/magic/" + mtgCard.getBlock().replace('+', '-') + "/" + mtgCard.getCard().replace('+', ' ');
        String[] parts = url.split(" ");
        StringBuilder builder = new StringBuilder();
        Arrays.stream(parts).forEach(part -> { builder.append(part); builder.append("%20");});
        return builder.toString().substring(0, builder.toString().length()-3);
    }

    public MTGCardPurgeResponseDTO purgeCard(long cardID) {
        MTGCardPurgeResponseDTO mtgCardPurgeResponseDTO = new MTGCardPurgeResponseDTO();
        mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(true);

        MTGCollectionAsset mtgCollectionAsset = mtgCollectionAssetRepository.findTopByCardIDAndUserID(cardID, mtgUserService.GetUser().getId());
        if (mtgCollectionAsset != null) {
            mtgCollectionAssetRepository.delete(mtgCollectionAsset);
            mtgCardPurgeResponseDTO.setPurgeCollectionAssetSuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCollectionAssetSuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Collection Asset for cardID: " + cardID + " resulted in a null value");
        }

        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryRepository.findAllByCardIDOrderByModifiedDateDesc(cardID);
        if (mtgCardValueHistoryList != null) {
            mtgCardPurgeResponseDTO.setCardValueHistorySize(mtgCardValueHistoryList.size());
            mtgCardValueHistoryList.stream().forEach(mtgCardValueHistory -> mtgCardValueHistoryRepository.delete(mtgCardValueHistory));
            mtgCardPurgeResponseDTO.setPurgeCardValueHistorySuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCardValueHistorySuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Card Value Histories for cardID: " + cardID + " resulted in a null value");
        }

        MTGCard mtgCard = mtgCardRepository.findDistinctById(cardID);
        if (mtgCard != null) {
            mtgCardRepository.delete(mtgCard);
            mtgCardPurgeResponseDTO.setPurgeCardSuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCardSuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Card for cardID: " + cardID + " resulted in a null value");
        }

        return mtgCardPurgeResponseDTO;
    }

    public MTGGoldfishURLUpdateDTO updateMTGGoldfishURL(long cardID, String mtgURL) {
        MTGGoldfishURLUpdateDTO mtgGoldfishURLUpdateDTO = new MTGGoldfishURLUpdateDTO();
        MTGCard mtgCard = mtgCardRepository.findDistinctById(cardID);
        if (mtgCard != null) {
            mtgGoldfishURLUpdateDTO.setOldURL(mtgCard.getMtgGoldfishURL());
            mtgGoldfishURLUpdateDTO.setNewURL(mtgURL);
            mtgCard.setMtgGoldfishURL(mtgURL);
            mtgCardRepository.save(mtgCard);
        }

        return mtgGoldfishURLUpdateDTO;
    }
}
