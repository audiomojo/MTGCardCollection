package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.helpers.TextFormatting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGCollectionAssetService {

    @Autowired
    MTGCollectionAssetRepository mtgCollectionAssetRepository;

    @Autowired
    MTGCardRepository mtgCardRepository;

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
        List<String> dropDownOptions = filterAndSortNotesDropDownOptions(mtgCollectionAssetRepository.findAllByUserID(mtgUser.getId()));

        String result = "<option value=\"Type Free Text Below...\" >Type Free Text Below</option>";
        for (String dropDownOption : dropDownOptions) {
            result += "<option value=\"" + dropDownOption + "\" >" + dropDownOption + "</option>";
        }
        return result;
    }

    private List<String> filterAndSortNotesDropDownOptions(List<MTGCollectionAsset> mtgCollectionAssetList) {
        List<String> dropDownOptions = new ArrayList<>();

        for (MTGCollectionAsset mtgCollectionAsset : mtgCollectionAssetList) {
            String[] optionSegments = mtgCollectionAsset.getNotes().split("\\r?\\n");
            if ((optionSegments.length >= 2) && (optionSegments[1].compareTo("") != 0) && dropDownOptions.contains(optionSegments[1]) == false)
                dropDownOptions.add(optionSegments[1]);
        }

        dropDownOptions.sort((s1, s2) -> s1.compareTo(s2));

        return dropDownOptions;
    }

    public String totalCollectionValue(MTGUser mtgUser) {
        double total = 0;

        List<MTGCollectionAsset>mtgCollectionAssetList = mtgCollectionAssetRepository.findAllByUserID(mtgUser.getId());
        for (MTGCollectionAsset mtgCollectionAsset : mtgCollectionAssetList) {
            MTGCard mtgCard = mtgCardRepository.findDistinctById(mtgCollectionAsset.getCardID());
            total = total + (mtgCollectionAsset.getQuantity() * mtgCard.getMostRecentValue());
        }

        return TextFormatting.FormatAsUSD(total);
    }
}
