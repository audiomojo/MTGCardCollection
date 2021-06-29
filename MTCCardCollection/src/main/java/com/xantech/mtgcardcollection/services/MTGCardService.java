package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.config.ScheduleProperties;
import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.enums.CollectionAdjustment;
import com.xantech.mtgcardcollection.helpers.MTGGoldFishCardValueEngine;
import com.xantech.mtgcardcollection.helpers.MTGGoldfishURLParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    MTGGoldFishCardValueEngine mtgGoldFishCardValueEngine;

    @Autowired
    ScheduleProperties scheduleProperties;

    public MTGCardDTO lookupCard(String url, MTGUser mtgUser) {
        MTGCard mtgCard = mtgCardRepository.findDistinctByMtgGoldfishURL(url);
        MTGCollectionAsset mtgCollectionAsset = null;

        if (mtgCard != null) {
           mtgCollectionAsset = mtgCollectionAssetService.getMtgCollectionAssetRepository().findTopByCardIDAndUserID(mtgCard.getId(), mtgUser.getId());
        }

        return mtgCardDTOService.AssembleMTGCardDTO(mtgCard, mtgCollectionAsset, mtgUser);
    }

    public MTGCardDTO adjustcollection(CollectionAdjustment adjustment, String url, int count, String notes, MTGUser mtgUser) {
        Date date = new Date();
        MTGCard mtgCard = getMTGCard(url, date);
        MTGCollectionAsset mtgCollectionAsset = null;

        if (adjustment == CollectionAdjustment.ADD) {
            mtgCollectionAsset = mtgCollectionAssetService.AddCollectionAsset(mtgCard, mtgUser, count, notes, date);
            updateCardValue(mtgCard, date);
        } else if (adjustment == CollectionAdjustment.REMOVE) {
            mtgCollectionAsset = mtgCollectionAssetService.RemoveCollectionAsset(mtgCard, mtgUser, count, notes, date);
        }

        return mtgCardDTOService.AssembleMTGCardDTO(mtgCard, mtgCollectionAsset, mtgUser);
    }

    private MTGCard getMTGCard(String url, Date date) {
        MTGCard mtgCard = mtgCardRepository.findDistinctByMtgGoldfishURL(url);

        if (mtgCard == null){
            mtgCard = createMTGCard(url, date);
        }
        return mtgCard;
    }


    private MTGCard createMTGCard(String url, Date date) {
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
        mtgCard.setImageURL(mtgGoldFishCardValueEngine.getImageURL(mtgCard));
        mtgCardRepository.save(mtgCard);
        return mtgCard;
    }

    private void updateCardValueMetrics(MTGCard mtgCard, Date date) {
        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryService.GetMTGCardValueHistory(mtgCard);
        mtgCard.setLastValueCheck(date);
        mtgCard.setTwentyFourHourValueShift(calculateValueShift(mtgCardValueHistoryList,1));
        mtgCard.setTwentyFourHourPercentageShift(calculatePercentageShift(mtgCardValueHistoryList,1));
        mtgCard.setSevenDayValueShift(calculateValueShift(mtgCardValueHistoryList, 7));
        mtgCard.setSevenDayHourPercentageShift(calculatePercentageShift(mtgCardValueHistoryList, 7));
        mtgCard.setThirtyDayValueShift(calculateValueShift(mtgCardValueHistoryList, 30));
        mtgCard.setThirtyDayPercentageShift(calculatePercentageShift(mtgCardValueHistoryList, 30));
        mtgCard.setAllTimeValueShift(calculateValueShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
        mtgCard.setAllTimePercentageShift(calculatePercentageShift(mtgCardValueHistoryList, mtgCardValueHistoryList.size()-1));
        try {
            mtgCard.setMostRecentValue(mtgCardValueHistoryList.get(0).getValue());
        } catch (IndexOutOfBoundsException ex) {
            log.error("Error:  Card Value History List is empty for " + mtgCard.getCard());
        }
        mtgCardRepository.save(mtgCard);
    }

    private double calculateValueShift(List<MTGCardValueHistory> mtgCardValueHistoryList, int days){
        double valueShift = 0.0;

        if ((days > 0) && (mtgCardValueHistoryList.size() >= days+1)){
            valueShift = mtgCardValueHistoryList.get(0).getValue() - mtgCardValueHistoryList.get(days).getValue();
        }

        return valueShift;
    }

    private double calculatePercentageShift(List<MTGCardValueHistory> mtgCardValueHistoryList, int days){
        double valueShift = 0.0;

        if ((days > 0) && (mtgCardValueHistoryList.size() >= days+1)){
            valueShift = ((mtgCardValueHistoryList.get(0).getValue()-mtgCardValueHistoryList.get(days).getValue())/mtgCardValueHistoryList.get(days).getValue());
        }

        return valueShift;
    }

    public List<MTGCard> getcollection() {
        return mtgCardRepository.findAllBy();
    }

    public List<MTGCard> updateCardValues(){
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        Date date = new Date();
        int index = 1;

        mtgCardList = mtgCardList.stream().sorted(Comparator.comparing(MTGCard::getCard)).sorted(Comparator.comparing(MTGCard::getBlock)).collect(Collectors.toList());

        for (MTGCard mtgCard : mtgCardList) {
            log.info("Updating card value [" + index++ + " of " + mtgCardList.size() + "] --- " + mtgCard.toString());
            if (updatedValueNeeded(mtgCard,date)) {
                updateCardValue(mtgCard, date);
            }
        }

        return mtgCardList;
    }

    private boolean updatedValueNeeded(MTGCard mtgCard, Date date) {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        if (dateTimeComparator.compare(date, mtgCard.getLastValueCheck()) == 0)
            return false;
        else
            return true;
//        Long diff = date.getTime() - mtgCard.getLastValueCheck().getTime();
//        return diff > scheduleProperties.getDataValidInterval();
    }

    private void updateCardValue(MTGCard mtgCard, Date date){
        mtgCardValueHistoryService.updateCardValue(mtgCard, date);
        updateCardValueMetrics(mtgCard, date);
    }

    public MTGCard getMTGCardByID(long cardID) {
        MTGCard result = null;

        result = mtgCardRepository.findDistinctById(cardID);

        return result;
    }
}
