package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.dto.MTGDeckAssetDTO;
import com.xantech.mtgcardcollection.dto.MTGDeckListDTO;
import com.xantech.mtgcardcollection.helpers.TextFormatting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGDeckService {
    @Autowired
    MTGDeckRepository mtgDeckRepository;

    @Autowired
    MTGDeckAssetRepository mtgDeckAssetRepository;

    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGDeckTypeRepository mtgDeckTypeRepository;

    public MTGDeck addDeck(String deckName, String notes, String deckTypeID, MTGUser mtgUser) {
        Date date = new Date();
        MTGDeck mtgDeck = mtgDeckRepository.findTopByNameAndUserID(deckName, mtgUser.getId());

        if (mtgDeck == null) {
            mtgDeck = new MTGDeck();
            mtgDeck.setCreatedDate(date);
            mtgDeck.setName(deckName);
            mtgDeck.setUserID(mtgUser.getId());
        }
        mtgDeck.setModifiedDate(date);
        mtgDeck.setDeckTypeID(Integer.parseInt(deckTypeID));
        mtgDeck.setNotes(notes);
        mtgDeckRepository.save(mtgDeck);
        return mtgDeck;
    }

    public String GetDeckDropDownOptions(MTGUser mtgUser) {
        List<MTGDeck> mtgDeckList = mtgDeckRepository.findAllByUserIDOrderByModifiedDateDesc(mtgUser.getId());

        String result = "<option value=\"no-deck\">Select deck from list if card is in a deck</option>";
        for (MTGDeck mtgDeck : mtgDeckList) {
            result += "<option value=\"" + mtgDeck.getId() + "\" >" + mtgDeck.getName() + "</option>";
        }
        return result;
    }

    public MTGDeck GetDeck(long deckID) {
        return mtgDeckRepository.findTopById(deckID);
    }

    public MTGDeckListDTO getDeckList(String deckName) {
        if (deckName != "no-deck") {
            MTGDeckListDTO mtgDeckListDTO = new MTGDeckListDTO();
            List<MTGDeckAssetDTO> mtgDeckAssetDTOList = new ArrayList<>();
            mtgDeckListDTO.setMtgDeckAssetDTOList(mtgDeckAssetDTOList);

            MTGDeck mtgDeck = mtgDeckRepository.findTopById(Integer.parseInt(deckName));
            mtgDeckListDTO.setDeckName(mtgDeck.getName());
            double deckValue = 0;

            List<MTGDeckAsset> mtgDeckAssetList = mtgDeckAssetRepository.findAllByDeckID(mtgDeck.getId());
            for (MTGDeckAsset mtgDeckAsset : mtgDeckAssetList) {
                MTGCard mtgCard = mtgCardRepository.findDistinctById(mtgDeckAsset.getCardID());
                MTGDeckAssetDTO mtgDeckAssetDTO = new MTGDeckAssetDTO();
                mtgDeckAssetDTO.setBlock(mtgCard.getBlock());
                mtgDeckAssetDTO.setCard(mtgCard.getCard());
                mtgDeckAssetDTO.setValue(mtgCard.getMostRecentValue());
                mtgDeckAssetDTO.setQuantity(mtgDeckAsset.getQuantity());
                mtgDeckAssetDTOList.add(mtgDeckAssetDTO);
                deckValue += mtgCard.getMostRecentValue() * mtgDeckAsset.getQuantity();
            }

            mtgDeckListDTO.setDeckValue(deckValue);
            mtgDeckListDTO.setDeckValueStr(TextFormatting.FormatAsUSD(deckValue));

            return mtgDeckListDTO;
        }
        else
            return null;
    }

    public String GetDeckTypeDropDownOptions(MTGUser mtgUser) {
        List<MTGDeckType> mtgDeckTypeList = mtgDeckTypeRepository.findAllByUserID(mtgUser.getId());
        mtgDeckTypeList.sort(Comparator.comparing(mtgDeckType -> mtgDeckType.getModifiedDate()));

        String result = "<option value=\"no-deck-type\">Select deck type from list...</option>";
        for (MTGDeckType mtgDeckType : mtgDeckTypeList) {
            result += "<option value=\"" + mtgDeckType.getId() + "\" >" + mtgDeckType.getDeckType() + "</option>";
        }
        return result;
    }
}
