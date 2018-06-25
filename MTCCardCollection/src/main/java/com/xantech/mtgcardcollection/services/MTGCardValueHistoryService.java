package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistory;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistoryRepository;
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

    private MTGCardValueHistory mtgCardValueHistory;

    public MTGCardValueHistory updateCardValue(MTGCard mtgCard) {

        double cardValue = mtgGoldFishCardValueEngine.getCardValue(mtgCard);

        mtgCardValueHistory = mtgCardValueHistoryRepository.findTopByCardIDOrderByModifiedDateDesc(mtgCard.getId());

        if ((mtgCardValueHistory == null) || (SameDay(mtgCardValueHistory.getDate()) == false)) {
            mtgCardValueHistory = new MTGCardValueHistory();
            mtgCardValueHistory.setCardID(mtgCard.getId());
            mtgCardValueHistory.setCreatedDate(mtgCard.getModifiedDate());
            mtgCardValueHistory.setDate(mtgCard.getModifiedDate());
        }

        mtgCardValueHistory.setModifiedDate(mtgCard.getModifiedDate());
        mtgCardValueHistory.setValue(cardValue);
        mtgCardValueHistoryRepository.save(mtgCardValueHistory);

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


}
