package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCardRepository;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistory;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistoryRepository;
import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.data.objects.ValueNode;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.enums.CollectionAdjustment;
import com.xantech.mtgcardcollection.helpers.MTGGoldFishCardValueEngine;
import com.xantech.mtgcardcollection.helpers.MTGGoldfishURLParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGCardService {

    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGCardValueHistoryService mtgCardValueHistoryService;

    @Autowired
    MTGCardDTOService mtgCardDTOService;

    private MTGCard mtgCard;

    private MTGCard LookupCard(String url) {
//        CardCollection cardCollection = new CardCollection();
//        return cardCollection.LookupCard(MTGGoldfishURLParser.GetBlock(url), MTGGoldfishURLParser.GetCard(url), MTGGoldfishURLParser.GetFormat(url));
        return null;
    }

    public MTGCardDTO AdjustCollection(CollectionAdjustment adjustment, String url, int count, String notes) {
        mtgCard = mtgCardRepository.findDistinctByMtgGoldfishURL(url);

        if ((adjustment == CollectionAdjustment.ADD) || ((adjustment == CollectionAdjustment.REMOVE) && (mtgCard != null))) {

            Date date = new Date();

            if (!((adjustment == CollectionAdjustment.REMOVE) && (mtgCard.getQuantity() == 0))) {

                if ((mtgCard == null) && (adjustment == CollectionAdjustment.ADD)) {
                    mtgCard = CreateCard(url, count, notes);
                    mtgCard.setCreatedDate(date);
                } else {
                    if (adjustment == CollectionAdjustment.ADD)
                        mtgCard.setQuantity(mtgCard.getQuantity() + count);
                    else if ((adjustment == CollectionAdjustment.REMOVE) && (mtgCard.getQuantity() > 0))
                        mtgCard.setQuantity(mtgCard.getQuantity() - count);

                    String currentNotes = mtgCard.getNotes();

                    if (currentNotes == null)
                        mtgCard.setNotes(date.toString() + "\n" + notes);
                    else
                        mtgCard.setNotes(currentNotes + "\n\n" + date.toString() + "\n" + notes);
                }

                mtgCard.setModifiedDate(date);
                System.out.println("Adding Card: " + mtgCard.toString());

                String transactionHistory = mtgCard.getTransactionHistory();

                if (adjustment == CollectionAdjustment.ADD) {
                    if (transactionHistory == null)
                        transactionHistory = mtgCard.getModifiedDate().toString() + "\n" + "Adding " + count + " Card(s)... New Card Total: " + mtgCard.getQuantity();
                    else
                        transactionHistory = transactionHistory + "\n\n" + mtgCard.getModifiedDate().toString() + "\n" + "Adding " + count + " Card(s)... New Card Total: " + mtgCard.getQuantity();
                } else if (adjustment == CollectionAdjustment.REMOVE) {
                    if (transactionHistory == null)
                        transactionHistory = mtgCard.getModifiedDate().toString() + "\n" + "Removing " + count + " Card(s)... New Card Total: " + mtgCard.getQuantity();
                    else
                        transactionHistory = transactionHistory + "\n\n" + mtgCard.getModifiedDate().toString() + "\n" + "Removing " + count + " Card(s)... New Card Total: " + mtgCard.getQuantity();
                }

                mtgCard.setTransactionHistory(transactionHistory);
                mtgCardRepository.save(mtgCard);
                mtgCardValueHistoryService.updateCardValue(mtgCard);
                UpdateCardValueMetrics(mtgCard);
            }

            return mtgCardDTOService.AssembleMTGCardDTO(mtgCard);
        }

        MTGCardDTO result = new MTGCardDTO();
        result.setNotes("ERROR: Attempting to remove card from collection that does not exist");
        return result;
    }

    private void UpdateCardValueMetrics(MTGCard mtgCard) {
        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryService.GetMTGCardValueHistory(mtgCard);
        mtgCard.setTwentyFourHourValueShift(CalculateValueShift(mtgCardValueHistoryList,1));
        mtgCard.setTwentyFourHourPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList,1));
        mtgCard.setSevenDayValueShift(CalculateValueShift(mtgCardValueHistoryList, 7));
        mtgCard.setSevenDayHourPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, 7));
        mtgCard.setThirtyDayValueShift(CalculateValueShift(mtgCardValueHistoryList, 30));
        mtgCard.setThirtyDayPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, 30));
        mtgCard.setAllTimeValueShift(CalculateValueShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
        mtgCard.setAllTimePercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
    }

    private double CalculateValueShift(List<MTGCardValueHistory> mtgCardValueHistoryList, int days){
        double valueShift = 0.0;

        if ((days > 0) && (mtgCardValueHistoryList.size() >= days+1)){
            valueShift = mtgCardValueHistoryList.get(0).getValue() - mtgCardValueHistoryList.get(days).getValue();
        }

        return valueShift;
    }

    private double CalculatePercentageShift(List<MTGCardValueHistory> mtgCardValueHistoryList, int days){
        double valueShift = 0.0;

        if ((days > 0) && (mtgCardValueHistoryList.size() >= days+1)){
            valueShift = mtgCardValueHistoryList.get(0).getValue() / mtgCardValueHistoryList.get(days).getValue()-1;
        }

        return valueShift;
    }


    private MTGCard CreateCard(String url, int count, String notes) {
        MTGCard mtgCard =  new MTGCard();
        mtgCard.setBlock(MTGGoldfishURLParser.GetBlock(url));
        mtgCard.setCard(MTGGoldfishURLParser.GetCard(url));
        mtgCard.setFormat(MTGGoldfishURLParser.GetFormat(url));
        mtgCard.setQuantity(count);
        mtgCard.setMtgGoldfishURL(url);
        mtgCard.setNotes(notes);
        return mtgCard;
    }
}
