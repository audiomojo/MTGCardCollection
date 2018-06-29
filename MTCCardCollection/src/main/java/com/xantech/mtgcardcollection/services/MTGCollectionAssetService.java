package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCollectionAsset;
import com.xantech.mtgcardcollection.dao.MTGCollectionAssetRepository;
import com.xantech.mtgcardcollection.dao.MTGUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Data
public class MTGCollectionAssetService {

    @Autowired
    MTGCollectionAssetRepository mtgCollectionAssetRepository;

    public MTGCollectionAsset AddCollectionAsset(MTGCard mtgCard, MTGUser mtgUser, int count, String notes, Date date) {
        MTGCollectionAsset mtgCollectionAsset = GetMTGCollectionAsset(mtgCard, mtgUser, date);
        mtgCollectionAsset.setQuantity(mtgCollectionAsset.getQuantity()+count);

        if (mtgCollectionAsset.getNotes() == "")
            mtgCollectionAsset.setNotes(date.toString() + "\n" + notes);
        else
            mtgCollectionAsset.setNotes(mtgCollectionAsset.getNotes() + "\n\n" + date.toString() + "\n" + notes);
        mtgCollectionAsset.setTransactionHistory(mtgCollectionAsset.getTransactionHistory() + "\n\n" + date.toString() + "\n" + "Adding " + count + " Card(s)... New Card Total: " + count);
        mtgCollectionAsset.setModifiedDate(date);
        mtgCollectionAssetRepository.save(mtgCollectionAsset);

        return mtgCollectionAsset;
    }

    public MTGCollectionAsset RemoveCollectionAsset(MTGCard mtgCard, MTGUser mtgUser, int count, String notes, Date date) {
        MTGCollectionAsset mtgCollectionAsset = GetMTGCollectionAsset(mtgCard, mtgUser, date);
        mtgCollectionAsset.setQuantity(mtgCollectionAsset.getQuantity()-count);
        if (mtgCollectionAsset.getQuantity() < 0)
            mtgCollectionAsset.setQuantity(0);

        if (mtgCollectionAsset.getNotes() == "")
            mtgCollectionAsset.setNotes(date.toString() + "\n" + notes);
        else
            mtgCollectionAsset.setNotes(mtgCollectionAsset.getNotes() + "\n\n" + date.toString() + "\n" + notes);
        mtgCollectionAsset.setTransactionHistory(mtgCollectionAsset.getTransactionHistory() + "\n\n" + date.toString() + "\n" + "Removing " + count + " Card(s)... New Card Total: " + count);
        mtgCollectionAsset.setModifiedDate(date);
        mtgCollectionAssetRepository.save(mtgCollectionAsset);

        return mtgCollectionAsset;
    }

    private MTGCollectionAsset GetMTGCollectionAsset(MTGCard mtgCard, MTGUser mtgUser, Date date) {
        MTGCollectionAsset mtgCollectionAsset = mtgCollectionAssetRepository.findTopByCardIDAndUserID(mtgCard.getId(), mtgUser.getId());

        if (mtgCollectionAsset == null){
            mtgCollectionAsset = CreateMTGCollectionAsset(mtgCard, mtgUser, date);
        }
        return mtgCollectionAsset;
    }

    private MTGCollectionAsset CreateMTGCollectionAsset(MTGCard mtgCard, MTGUser mtgUser, Date date) {
        MTGCollectionAsset mtgCollectionAsset = new MTGCollectionAsset();
        mtgCollectionAsset.setCardID(mtgCard.getId());
        mtgCollectionAsset.setUserID(mtgUser.getId());
        mtgCollectionAsset.setQuantity(0);
        mtgCollectionAsset.setNotes("");
        mtgCollectionAsset.setTransactionHistory(date.toString() + "\nCard Created...");
        mtgCollectionAsset.setCreatedDate(date);
        mtgCollectionAsset.setModifiedDate(date);
        mtgCollectionAssetRepository.save(mtgCollectionAsset);
        return mtgCollectionAsset;
    }


}
