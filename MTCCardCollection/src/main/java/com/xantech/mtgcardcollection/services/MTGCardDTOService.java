package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.dto.MTGCardValueHistoryDTO;
import com.xantech.mtgcardcollection.dto.MTGDeckDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class MTGCardDTOService {

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    @Autowired
    MTGDeckAssetRepository mtgDeckAssetRepository;

    @Autowired
    MTGDeckRepository mtgDeckRepository;

    private MTGCardDTO mtgCardDTO;

    public MTGCardDTO AssembleMTGCardDTO(MTGCard mtgCard, MTGCollectionAsset mtgCollectionAsset, MTGUser mtgUser) {
        mtgCardDTO = new MTGCardDTO();
        CopyMTGCard(mtgCard, mtgCollectionAsset);
        CopyMTGCardValueHistoryList(mtgCard);
        CopyMTGDeckList(mtgCard, mtgUser);

        return mtgCardDTO;
    }

    private void CopyMTGDeckList(MTGCard mtgCard, MTGUser mtgUser) {
        if (mtgCard != null) {
            List<MTGDeckDTO> mtgDeckDTOList = new ArrayList<>();
            //List<MTGDeckAsset> mtgDeckAssetList = mtgDeckAssetRepository.findAllByCardIDAndUserID(mtgDeckAsset.getCardID(), mtgDeckAsset.getUserID());
            List<MTGDeckAsset> mtgDeckAssetList = mtgDeckAssetRepository.findAllByCardIDAndUserID(mtgCard.getId(), mtgUser.getId());
            for (MTGDeckAsset asset : mtgDeckAssetList) {
                MTGDeckDTO mtgDeckDTO = new MTGDeckDTO();
                MTGDeck mtgDeck = mtgDeckRepository.findTopById(asset.getDeckID());
                mtgDeckDTO.setDeckName(mtgDeck.getName());
                mtgDeckDTOList.add(mtgDeckDTO);
            }
            mtgCardDTO.setDeckDTOList(mtgDeckDTOList);
        }
    }

    private void CopyMTGCardValueHistoryList(MTGCard mtgCard) {
        if (mtgCard != null) {
        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryRepository.findAllByCardIDOrderByModifiedDateDesc(mtgCard.getId());
        List<MTGCardValueHistoryDTO> mtgCardValueHistoryDTOList = new ArrayList<>();
        mtgCardDTO.setValueHistoryDTOList(mtgCardValueHistoryDTOList);
        for (MTGCardValueHistory mtgCardValueHistory : mtgCardValueHistoryList) {
            mtgCardValueHistoryDTOList.add(CopyMTGValueHistory(mtgCardValueHistory));
        }}
    }

    private MTGCardValueHistoryDTO CopyMTGValueHistory(MTGCardValueHistory mtgCardValueHistory) {
        MTGCardValueHistoryDTO mtgCardValueHistoryDTO = new MTGCardValueHistoryDTO();

        mtgCardValueHistoryDTO.setValue(mtgCardValueHistory.getValue());
        mtgCardValueHistoryDTO.setDate(mtgCardValueHistory.getDate());
        mtgCardValueHistoryDTO.setDateString(mtgCardValueHistory.getDate().toString());

        return mtgCardValueHistoryDTO;
    }

    private void CopyMTGCard(MTGCard mtgCard, MTGCollectionAsset mtgCollectionAsset) {
        if (mtgCard != null) {
            mtgCardDTO.setBlock(mtgCard.getBlock());
            mtgCardDTO.setCard(mtgCard.getCard());
            mtgCardDTO.setFormat(mtgCard.getFormat());
            mtgCardDTO.setCurrentValue(mtgCard.getMostRecentValue());
            mtgCardDTO.setTwentyFourHourValueShift(mtgCard.getTwentyFourHourValueShift());
            mtgCardDTO.setTwentyFourHourPercentageShift(mtgCard.getTwentyFourHourPercentageShift());
            mtgCardDTO.setSevenDayValueShift(mtgCard.getSevenDayValueShift());
            mtgCardDTO.setSevenDayHourPercentageShift(mtgCard.getSevenDayHourPercentageShift());
            mtgCardDTO.setThirtyDayValueShift(mtgCard.getThirtyDayValueShift());
            mtgCardDTO.setThirtyDayPercentageShift(mtgCard.getThirtyDayPercentageShift());
            mtgCardDTO.setAllTimeValueShift(mtgCard.getAllTimeValueShift());
            mtgCardDTO.setAllTimePercentageShift(mtgCard.getAllTimePercentageShift());
            mtgCardDTO.setMtgGoldfishURL(mtgCard.getMtgGoldfishURL());
            mtgCardDTO.setMtgImageURL(mtgCard.getImageURL());
        }

        if (mtgCollectionAsset != null) {
            mtgCardDTO.setQuantity(mtgCollectionAsset.getQuantity());
            mtgCardDTO.setNotes(mtgCollectionAsset.getNotes());
        }
    }
}
