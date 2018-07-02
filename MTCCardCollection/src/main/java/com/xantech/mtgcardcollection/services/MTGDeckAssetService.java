package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Data
public class MTGDeckAssetService {
    @Autowired
    MTGDeckAssetRepository mtgDeckAssetRepository;

    public MTGDeckAsset AddDeckAsset(MTGCard mtgCard, MTGDeck mtgDeck, MTGUser mtgUser, int count, Date date) {
        MTGDeckAsset mtgDeckAsset = GetMTGDeckAsset(mtgCard, mtgUser, mtgDeck, date);
        mtgDeckAsset.setQuantity(mtgDeckAsset.getQuantity()+count);
        mtgDeckAsset.setTransactionHistory(mtgDeckAsset.getTransactionHistory() + "\n\n" + date.toString() + "\n" + "Adding " + count + " Card(s) to Deck... New Card Total: " + count);
        mtgDeckAsset.setModifiedDate(date);
        mtgDeckAssetRepository.save(mtgDeckAsset);
        return mtgDeckAsset;
    }

    public MTGDeckAsset RemoveDeckAsset(MTGCard mtgCard, MTGDeck mtgDeck, MTGUser mtgUser, int count, Date date) {
        MTGDeckAsset mtgDeckAsset = GetMTGDeckAsset(mtgCard, mtgUser, mtgDeck, date);
        mtgDeckAsset.setQuantity(mtgDeckAsset.getQuantity()-count);
        if (mtgDeckAsset.getQuantity() < 0)
            mtgDeckAsset.setQuantity(0);
        mtgDeckAsset.setTransactionHistory(mtgDeckAsset.getTransactionHistory() + "\n\n" + date.toString() + "\n" + "Removing " + count + " Card(s)... New Card Total: " + count);
        mtgDeckAsset.setModifiedDate(date);
        mtgDeckAssetRepository.save(mtgDeckAsset);
        return mtgDeckAsset;
    }

    private MTGDeckAsset GetMTGDeckAsset(MTGCard mtgCard, MTGUser mtgUser, MTGDeck mtgDeck, Date date) {
        MTGDeckAsset mtgDeckAsset = mtgDeckAssetRepository.findTopByCardIDAndUserIDAndDeckID(mtgCard.getId(), mtgUser.getId(), mtgDeck.getId());

        if (mtgDeckAsset == null){
            mtgDeckAsset = CreateMTGCDeckAsset(mtgCard, mtgUser, mtgDeck, date);
        }
        return mtgDeckAsset;
    }

    private MTGDeckAsset CreateMTGCDeckAsset(MTGCard mtgCard, MTGUser mtgUser, MTGDeck mtgDeck, Date date) {
        MTGDeckAsset mtgDeckAsset = new MTGDeckAsset();
        mtgDeckAsset.setCardID(mtgCard.getId());
        mtgDeckAsset.setUserID(mtgUser.getId());
        mtgDeckAsset.setDeckID(mtgDeck.getId());
        mtgDeckAsset.setQuantity(0);
        mtgDeckAsset.setTransactionHistory(date.toString() + "\nAdding Card to Deck: " + mtgDeck.getName());
        mtgDeckAsset.setCreatedDate(date);
        mtgDeckAsset.setModifiedDate(date);
        mtgDeckAssetRepository.save(mtgDeckAsset);
        return mtgDeckAsset;
    }

}
