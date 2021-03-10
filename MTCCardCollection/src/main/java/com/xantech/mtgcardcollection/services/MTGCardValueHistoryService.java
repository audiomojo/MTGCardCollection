package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.config.MTGGoldfishHarvestProperties;
import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.helpers.MTGGoldFishCardValueEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MTGCardValueHistoryService {
    @Autowired
    MTGGoldFishCardValueEngine mtgGoldFishCardValueEngine;

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGGoldfishHarvestProperties mtgGoldfishHarvestProperties;

    private MTGCardValueHistory mtgCardValueHistory;

    public MTGCardValueHistory updateCardValue(MTGCard mtgCard, Date date) {
        int tries = 0;
        double cardValue = 0;
        int sleepCount = mtgGoldfishHarvestProperties.getThrottelingPause();
        int maxTries = 5;

        do {
            cardValue = mtgGoldFishCardValueEngine.getCardValue(mtgCard);
            tries++;
            if (cardValue == 0) {
                System.out.println("Try # " + tries + " returned a value of 0 which indicates throttling from MTGGoldfish.  Sleeping for " + sleepCount*tries/1000 + " seconds then resuming");
                try {
                    Thread.sleep(sleepCount*tries);
                } catch (InterruptedException ex) {
                    System.out.println("Thread Exception: " + ex.toString());
                }
            }
        } while ((cardValue == 0) && (tries < maxTries));

        mtgCardValueHistory = mtgCardValueHistoryRepository.findTopByCardIDOrderByModifiedDateDesc(mtgCard.getId());

        if ((mtgCardValueHistory == null) || ((SameDay(mtgCardValueHistory.getDate()) == false) && (mtgCard.getMostRecentValue() != -1.0))) {
            mtgCardValueHistory = new MTGCardValueHistory();
            mtgCardValueHistory.setCardID(mtgCard.getId());
            mtgCardValueHistory.setCreatedDate(date);
            mtgCardValueHistory.setDate(date);
        }

        mtgCardValueHistory.setModifiedDate(date);
        mtgCardValueHistory.setValue(cardValue);
        mtgCard.setMostRecentValue(cardValue);
        mtgCard.setLastValueCheck(date);

        if (cardValue > 0) {
            mtgCardRepository.save(mtgCard);
            mtgCardValueHistoryRepository.save(mtgCardValueHistory);
        }

        return mtgCardValueHistory;
    }

    public List<MTGCardValueHistory> GetMTGCardValueHistory(MTGCard mtgCard) {
        return mtgCardValueHistoryRepository.findAllByCardIDOrderByModifiedDateDesc(mtgCard.getId());
    }

    private boolean SameDay(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        calendarDate.setTime(date);
        today.setTime(new Date());
        return calendarDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendarDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendarDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }


    public String GetMTGGoldfishCardPrice(String url) {
        MTGCard mtgCard = new MTGCard();
        mtgCard.setMtgGoldfishURL(url);
        mtgCard.setFormat("paper");
        return Double.toString(mtgGoldFishCardValueEngine.getCardValue(mtgCard));
    }
}
