package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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


    public String GetNoteDropDownOptions(MTGUser mtgUser) {
        List<MTGCollectionAsset> mtgCollectionAssetList = mtgCollectionAssetRepository.findAllByUserID(mtgUser.getId());
        List<MTGCollectionAsset> mtgCollectionAssetListFiltered = mtgCollectionAssetList.stream().filter(asset -> asset.getNotes().compareTo("") != 0).collect(Collectors.toList());

        String result = "<option value=\"Type Free Text Below...\" >Type Free Text Below</option>";
        String assetString = "";
        ArrayList<String> usedAssets = new ArrayList<>();
        for (MTGCollectionAsset asset : mtgCollectionAssetListFiltered) {

            String[] tokens = asset.getNotes().split("\n");
            if (tokens.length > 1)
                assetString = tokens[1];

            if (usedAssets.contains(assetString) == false) {
                result += "<option value=\"" + assetString + "\" >" + assetString + "</option>";
                usedAssets.add(assetString);
            }
            //result += "<option value=\"" + asset.getNotes() + "\" >" + asset.getNotes() + "</option>";
        }
        return result;
    }
}
