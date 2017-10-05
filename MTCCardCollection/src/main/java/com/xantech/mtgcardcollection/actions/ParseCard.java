package com.xantech.mtgcardcollection.actions;

import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.helpers.MTGGoldfishURLParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParseCard {
    public Card card = null;

    public ParseCard(String url, String count, String action) {
        if (action.compareTo("ADD") == 0)
            card = AddCard(url, count);
        else if (action.compareTo("DELETE") == 0)
            card = DeleteCard(url);
        else if (action.compareTo("DECREMENT") == 0)
            card = DecrementCard(url, count);
        else if (action.compareTo("LOOKUP") == 0)
            card = LookupCard(url);

    }

    private Card LookupCard(String url) {
        CardCollection cardCollection = new CardCollection();
        return cardCollection.LookupCard(MTGGoldfishURLParser.GetBlock(url), MTGGoldfishURLParser.GetCard(url), MTGGoldfishURLParser.GetFormat(url));
    }

    private Card DecrementCard(String url, String count) {
        CardCollection cardCollection = new CardCollection();
        return cardCollection.DecrementCard(MTGGoldfishURLParser.GetBlock(url), MTGGoldfishURLParser.GetCard(url), MTGGoldfishURLParser.GetFormat(url), count);
    }

    private Card DeleteCard(String url) {
        CardCollection cardCollection = new CardCollection();
        return cardCollection.DeleteCard(MTGGoldfishURLParser.GetBlock(url), MTGGoldfishURLParser.GetCard(url), MTGGoldfishURLParser.GetFormat(url));
    }


    private Card AddCard(String url, String count) {
        CardCollection cardCollection = new CardCollection();
        Card card = CreateCard(url, Integer.parseInt(count));
        System.out.println("Card: " + card.toString());
        if (cardCollection.AddCard(card))
            return card;
        else
            return null;
    }

    private Card CreateCard(String url, int count) {
        String block = MTGGoldfishURLParser.GetBlock(url);
        String card = MTGGoldfishURLParser.GetCard(url);
        String format = MTGGoldfishURLParser.GetFormat(url);
        return new Card(0, block, card, format, count);
    }
}
