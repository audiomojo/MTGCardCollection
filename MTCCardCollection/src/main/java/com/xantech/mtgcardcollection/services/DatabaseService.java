package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCardRepository;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistory;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistoryRepository;
import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.data.objects.ValueNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DatabaseService {

    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    public String transferFileToDatabase() {
        CardCollection cardCollection = new CardCollection();

        for (Card card : cardCollection.getCollection()) {
            Date date = new Date();
            MTGCard mtgCard = new MTGCard();
            mtgCard.setBlock(card.getBlock());
            mtgCard.setCard(card.getCard());
            mtgCard.setFormat(card.getFormat());
            //mtgCard.setQuantity(card.getQuantity());
            mtgCard.setTwentyFourHourValueShift(card.getCardValueMetrics().getTwentyFourHourValueShift());
            mtgCard.setTwentyFourHourPercentageShift(card.getCardValueMetrics().getTwentyFourHourPercentageShift());
            mtgCard.setSevenDayValueShift(card.getCardValueMetrics().getSevenDayValueShift());
            mtgCard.setSevenDayHourPercentageShift(card.getCardValueMetrics().getSevenDayHourPercentageShift());
            mtgCard.setThirtyDayValueShift(card.getCardValueMetrics().getThirtyDayValueShift());
            mtgCard.setThirtyDayPercentageShift(card.getCardValueMetrics().getThirtyDayPercentageShift());
            mtgCard.setAllTimeValueShift(card.getCardValueMetrics().getAllTimeValueShift());
            mtgCard.setAllTimePercentageShift(card.getCardValueMetrics().getAllTimePercentageShift());
            mtgCard.setMtgGoldfishURL("https://www.mtggoldfish.com/price/".concat(mtgCard.getBlock()).concat("/").concat(mtgCard.getCard()).concat("#").concat(mtgCard.getFormat()));
            mtgCard.setCreatedDate(date);
            mtgCard.setModifiedDate(date);

            mtgCardRepository.save(mtgCard);

            for(ValueNode valueNode: card.getValueHistory().getHistory()) {
                MTGCardValueHistory mtgCardValueHistory = new MTGCardValueHistory();
                mtgCardValueHistory.setCardID(mtgCard.getId());
                mtgCardValueHistory.setDate(valueNode.getDate());
                mtgCardValueHistory.setValue(valueNode.getValue());
                mtgCardValueHistory.setCreatedDate(date);
                mtgCardValueHistory.setModifiedDate(date);

                mtgCardValueHistoryRepository.save(mtgCardValueHistory);
            }

        }

        return "Database Migration Completed";
    }
}
