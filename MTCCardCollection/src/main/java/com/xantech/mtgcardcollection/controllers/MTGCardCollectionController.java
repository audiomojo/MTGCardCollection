package com.xantech.mtgcardcollection.controllers;

import com.xantech.mtgcardcollection.dao.MTGDeck;
import com.xantech.mtgcardcollection.dao.MTGUser;
import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.enums.CollectionAdjustment;
import com.xantech.mtgcardcollection.services.*;
import com.xantech.mtgcardcollection.view.reports.CardValueSummary;
import com.xantech.mtgcardcollection.helpers.ScreenScrapeCardValue;
import com.xantech.mtgcardcollection.view.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MTGCardCollectionController {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MTGCardService mtgCardService;

    @Autowired
    MTGUserService mtgUserService;

    @Autowired
    MTGCollectionReportService mtgCollectionReportService;

    @Autowired
    MTGCollectionAssetService mtgCollectionAssetService;

    @Autowired
    MTGDeckService mtgDeckService;

    @RequestMapping("/cardvalue")
    public ScreenScrapeCardValue screenScrapeCardValue(@RequestParam(value="block", defaultValue="Return+to+Ravnica") String block,
                                                       @RequestParam(value="Card", defaultValue="Jace+Architect+of+Thought") String card,
                                                       @RequestParam(value="format", defaultValue="#paper") String format) {
        return new ScreenScrapeCardValue(block, card, format);
    }

    @RequestMapping("/addCard")
    public MTGCardDTO processAddCard(@RequestParam(value="url") String url,
                                     @RequestParam(value="quantity") String quantity,
                                     @RequestParam(value="notes") String notes,
                                     @RequestParam(value="decks-drop-down") String deck){
        return  mtgCardService.AdjustCollection(CollectionAdjustment.ADD, url, Integer.parseInt(quantity), notes, mtgUserService.GetUser(), deck);
    }

    @RequestMapping("/deleteCard")
    public MTGCardDTO processDeleteCard(@RequestParam(value="url") String url,
                                        @RequestParam(value="quantity") String quantity,
                                        @RequestParam(value="notes") String notes,
                                        @RequestParam(value="decks-drop-down") String deck){
        return  mtgCardService.AdjustCollection(CollectionAdjustment.REMOVE, url, Integer.parseInt(quantity), notes, mtgUserService.GetUser(), deck);
    }

    @RequestMapping("/addUser")
    public MTGUser addUser(@RequestParam(value="userName") String userName,
                           @RequestParam(value="email") String email,
                           @RequestParam(value="mobile") String mobile,
                           @RequestParam(value="password") String password) {
        return mtgUserService.AddUser(userName, email, mobile, password);
    }

    @RequestMapping("/lookupCard")
    public MTGCardDTO lookupCard(@RequestParam(value="url") String url) {
        return mtgCardService.LookupCard(url, mtgUserService.GetUser());
    }

    @RequestMapping("/collectionSummary")
    public String cardValueSummary(@RequestParam(value="format", defaultValue = "CARD") String format,
                                   @RequestParam(value="override", defaultValue="FALSE") String override) {
        return new CardValueSummary(format).CardValueSummaryReportHTML(override);
    }

    @RequestMapping("/collectionSummaryMVC")
    public ModelAndView cardValueSummaryMVC(@RequestParam(value="format", defaultValue = "CARD") String format,
                                   @RequestParam(value="override", defaultValue="FALSE") String override) {
        ModelAndView mav = new ModelAndView("collection-summary");
        mav.addObject("cardCollection", mtgCollectionReportService.GetModel(format, override, mtgUserService.GetUser()));
        return mav;
    }

    @RequestMapping("/updateCollectionValues")
    public String updateCollectionValues(@RequestParam(value="override", defaultValue="FALSE") String override) {
        return new CardCollection().UpdateCollectionValues(override);
    }

//    @RequestMapping("/debugCardCollection")
//    public String Debug() {
//        return new CardCollection().DebugCardCollection();
//    }

    @RequestMapping("/decrementCardCount")
    public String decrementCardCount() { return DecrementCardCountUI.HTML(); }

    @RequestMapping("/transferFileToDatabase")
    public String transferFileToDatabase() {
        return databaseService.transferFileToDatabase();
    }

    @RequestMapping("/getNoteDropDownOptions")
    public String getNoteDropDownOptions() {
        return mtgCollectionAssetService.GetNoteDropDownOptions(mtgUserService.GetUser());
    }

    @RequestMapping("/addDeck")
    public MTGDeck addDeck(@RequestParam(value="deckName") String deckName,
                           @RequestParam(value="notes") String notes) {
        return mtgDeckService.addCard(deckName, notes, mtgUserService.GetUser());
    }
    @RequestMapping("/getDeckDropDownOptions")
    public String getDeckDropDownOptions() {
        return mtgDeckService.GetDeckDropDownOptions(mtgUserService.GetUser());
    }

}
