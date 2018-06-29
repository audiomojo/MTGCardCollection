package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.enums.CollectionAdjustment;
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

    @Autowired
    MTGCollectionAssetService mtgCollectionAssetService;

    private MTGCard LookupCard(String url) {
//        CardCollection cardCollection = new CardCollection();
//        return cardCollection.LookupCard(MTGGoldfishURLParser.GetBlock(url), MTGGoldfishURLParser.GetCard(url), MTGGoldfishURLParser.GetFormat(url));
        return null;
    }

    public MTGCardDTO AdjustCollection(CollectionAdjustment adjustment, String url, int count, String notes, MTGUser mtgUser) {
        Date date = new Date();
        MTGCard mtgCard = GetMTGCard(url, date);
        MTGCollectionAsset mtgCollectionAsset = null;

        if (adjustment == CollectionAdjustment.ADD) {
            mtgCollectionAsset = mtgCollectionAssetService.AddCollectionAsset(mtgCard, mtgUser, count, notes, date);
        } else if (adjustment == CollectionAdjustment.REMOVE) {
            mtgCollectionAsset = mtgCollectionAssetService.RemoveCollectionAsset(mtgCard, mtgUser, count, notes, date);
        }

        updateCardValue(mtgCard, date);
        return mtgCardDTOService.AssembleMTGCardDTO(mtgCard, mtgCollectionAsset);
    }

    private MTGCard GetMTGCard(String url, Date date) {
        MTGCard mtgCard = mtgCardRepository.findDistinctByMtgGoldfishURL(url);

        if (mtgCard == null){
            mtgCard = CreateMTGCard(url, date);
        }
        return mtgCard;
    }


    private MTGCard CreateMTGCard(String url, Date date) {
        MTGCard mtgCard =  new MTGCard();
        mtgCard.setBlock(MTGGoldfishURLParser.GetBlock(url));
        mtgCard.setCard(MTGGoldfishURLParser.GetCard(url));
        mtgCard.setFormat(MTGGoldfishURLParser.GetFormat(url));
        mtgCard.setMtgGoldfishURL(url);
        mtgCard.setCreatedDate(date);
        mtgCard.setModifiedDate(date);
        mtgCard.setTwentyFourHourValueShift(0);
        mtgCard.setTwentyFourHourPercentageShift(0);
        mtgCard.setSevenDayValueShift(0);
        mtgCard.setSevenDayHourPercentageShift(0);
        mtgCard.setThirtyDayValueShift(0);
        mtgCard.setThirtyDayPercentageShift(0);
        mtgCard.setAllTimeValueShift(0);
        mtgCard.setAllTimePercentageShift(0);
        mtgCard.setLastValueCheck(date);
        mtgCardRepository.save(mtgCard);
        return mtgCard;
    }

    private void UpdateCardValueMetrics(MTGCard mtgCard, Date date) {
        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryService.GetMTGCardValueHistory(mtgCard);
        mtgCard.setLastValueCheck(date);
        mtgCard.setTwentyFourHourValueShift(CalculateValueShift(mtgCardValueHistoryList,1));
        mtgCard.setTwentyFourHourPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList,1));
        mtgCard.setSevenDayValueShift(CalculateValueShift(mtgCardValueHistoryList, 7));
        mtgCard.setSevenDayHourPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, 7));
        mtgCard.setThirtyDayValueShift(CalculateValueShift(mtgCardValueHistoryList, 30));
        mtgCard.setThirtyDayPercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, 30));
        mtgCard.setAllTimeValueShift(CalculateValueShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
        mtgCard.setAllTimePercentageShift(CalculatePercentageShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
        mtgCard.setMostRecentValue(mtgCardValueHistoryList.get(0).getValue());
        mtgCardRepository.save(mtgCard);
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

    public List<MTGCard> UpdateCardValues(String override){
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        int index = 0;
        Date date = new Date();

        for (MTGCard mtgCard : mtgCardList) {
            if ((date.getTime() - mtgCard.getLastValueCheck().getTime() > 10800000) || (override.compareTo("TRUE") == 0)) { // 3Hours
                System.out.println(index++ + " of " + mtgCardList.size() + " : " + mtgCard.toString());
                updateCardValue(mtgCard, date);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    System.out.println("Thread Exception: " + ex.toString());
                }
            }
        }

        return mtgCardList;
    }

    private void updateCardValue(MTGCard mtgCard, Date date){
        mtgCardValueHistoryService.updateCardValue(mtgCard, date);
        UpdateCardValueMetrics(mtgCard, date);
    }
}
