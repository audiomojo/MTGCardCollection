package com.xantech.mtgcardcollection.controller;

import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.view.reports.CardValueSummary;
import com.xantech.mtgcardcollection.actions.ParseCard;
import com.xantech.mtgcardcollection.helpers.ScreenScrapeCardValue;
import com.xantech.mtgcardcollection.view.ui.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MTGCardCollectionController {

    @RequestMapping("/cardvalue")
    public ScreenScrapeCardValue screenScrapeCardValue(@RequestParam(value="block", defaultValue="Return+to+Ravnica") String block,
                                                       @RequestParam(value="Card", defaultValue="Jace+Architect+of+Thought") String card,
                                                       @RequestParam(value="format", defaultValue="#paper") String format) {
        return new ScreenScrapeCardValue(block, card, format);
    }

    @RequestMapping("/parseCard")
    public ParseCard parseCard(@RequestParam(value="url") String url,
                               @RequestParam(value="quantity") String quantity,
                               @RequestParam(value="action") String action){
        return new ParseCard(url, quantity, action);
    }


    // Views
    @RequestMapping("/addCard")
    public String addCard() {
        return AddCardUI.HTML();
    }

    @RequestMapping("/cardValueSummary")
    public String cardValueSummary(@RequestParam(value="format", defaultValue = "CARD") String format,
                                   @RequestParam(value="override", defaultValue="FALSE") String override) {
        return new CardValueSummary(format).CardValueSummaryReportHTML(override);
    }

    @RequestMapping("/updateCollectionValues")
    public String updateCollectionValues(@RequestParam(value="override", defaultValue="FALSE") String override) {
        return new CardCollection().UpdateCollectionValues(override);
    }

    @RequestMapping("/debugCardCollection")
    public String Debug() {
        return new CardCollection().DebugCardCollection();
    }

    @RequestMapping("/deleteCard")
    public String deleteCard() { return DeleteCardUI.HTML(); }

    @RequestMapping("/decrementCardCount")
    public String decrementCardCount() { return DecrementCardCountUI.HTML(); }

    @RequestMapping("/lookupCard")
    public String lookupCard() { return LookupCard.HTML(); }

    @RequestMapping("/help")
    public String help() { return Help.HTML(); }




}
