package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGDeck;
import com.xantech.mtgcardcollection.dao.MTGDeckRepository;
import com.xantech.mtgcardcollection.dao.MTGUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGDeckService {
    @Autowired
    MTGDeckRepository mtgDeckRepository;

    public MTGDeck addCard(String deckName, String notes, MTGUser mtgUser) {
        Date date = new Date();
        MTGDeck mtgDeck = mtgDeckRepository.findTopByNameAndUserID(deckName, mtgUser.getId());

        if (mtgDeck == null) {
            mtgDeck = new MTGDeck();
            mtgDeck.setCreatedDate(date);
            mtgDeck.setName(deckName);
            mtgDeck.setUserID(mtgUser.getId());
        }
        mtgDeck.setModifiedDate(date);
        mtgDeck.setNotes(notes);
        mtgDeckRepository.save(mtgDeck);
        return mtgDeck;
    }

    public String GetDeckDropDownOptions(MTGUser mtgUser) {
        List<MTGDeck> mtgDeckList = mtgDeckRepository.findAllByUserID(mtgUser.getId());

        String result = "<option value=\"no-deck\">Select deck from list if card is in a deck</option>";
        for (MTGDeck mtgDeck : mtgDeckList) {
            result += "<option value=\"" + mtgDeck.getName() + "\" >" + mtgDeck.getName() + "</option>";
        }
        return result;
    }

    public MTGDeck GetDeck(String deck, MTGUser mtgUser) {
        return mtgDeckRepository.findTopByNameAndUserID(deck, mtgUser.getId());
    }
}
