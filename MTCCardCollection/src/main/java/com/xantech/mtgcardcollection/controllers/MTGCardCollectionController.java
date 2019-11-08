package com.xantech.mtgcardcollection.controllers;

import com.xantech.mtgcardcollection.dao.MTGDeck;
import com.xantech.mtgcardcollection.dao.MTGUser;
import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.CardViewModel;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.dto.MTGDeckListDTO;
import com.xantech.mtgcardcollection.enums.CollectionAdjustment;
import com.xantech.mtgcardcollection.launchers.UpdateCardValues;
import com.xantech.mtgcardcollection.services.*;
import com.xantech.mtgcardcollection.view.reports.CardValueSummary;
import com.xantech.mtgcardcollection.view.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@Slf4j
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

    @Autowired
    MTGDeckAssetService mtgDeckAssetService;

    @Autowired
    MTGCardValueHistoryService mtgCardValueHistoryService;

    @Autowired
    UpdateCardValues updateCardValues;


    @RequestMapping("/addCard")
    public MTGCardDTO processAddCard(@RequestParam(value="url") String url,
                                     @RequestParam(value="quantity") String quantity,
                                     @RequestParam(value="notes") String notes){
        return  mtgCardService.adjustcollection(CollectionAdjustment.ADD, url, Integer.parseInt(quantity), notes, mtgUserService.GetUser());
    }

    @RequestMapping("/deleteCard")
    public MTGCardDTO processDeleteCard(@RequestParam(value="url") String url,
                                        @RequestParam(value="quantity") String quantity,
                                        @RequestParam(value="notes") String notes){
        return  mtgCardService.adjustcollection(CollectionAdjustment.REMOVE, url, Integer.parseInt(quantity), notes, mtgUserService.GetUser());
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
        return mtgCardService.lookupCard(url, mtgUserService.GetUser());
    }

    @RequestMapping("/getMTGGoldfishCardPrice")
    public String getMTGGoldfishCardPrice(@RequestParam(value="url") String url) {
        return mtgCardValueHistoryService.GetMTGGoldfishCardPrice(url);
    }

    @RequestMapping("/collectionModel")
    public List<CardViewModel> collectionModel(@RequestParam(value="format", defaultValue = "CARD") String format,
                                   @RequestParam(value="override", defaultValue="FALSE") String override) {
        return mtgCollectionReportService.GetModel(format, mtgUserService.GetUser());
    }

    @RequestMapping("/collectionSummary")
    public String cardValueSummary(@RequestParam(value="format", defaultValue = "CARD") String format) {
        List<CardViewModel> cardViewModelList = mtgCollectionReportService.GetModel(format, mtgUserService.GetUser());

        return new CardValueSummary(format).CardValueSummaryReportHTML(cardViewModelList);
    }

    @RequestMapping("/collectionSummaryMVC")
    public ModelAndView cardValueSummaryMVC(@RequestParam(value="format", defaultValue = "CARD") String format) {
        ModelAndView mav = new ModelAndView("collection-summary");
        mav.addObject("cardCollection", mtgCollectionReportService.GetModel(format, mtgUserService.GetUser()));
        return mav;
    }

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
                           @RequestParam(value="notes") String notes,
                           @RequestParam(value="deck-type-drop-down") String deckTypeID) {
        return mtgDeckService.addDeck(deckName, notes, deckTypeID, mtgUserService.GetUser());
    }
    @RequestMapping("/getDeckDropDownOptions")
    public String getDeckDropDownOptions() {
        return mtgDeckService.GetDeckDropDownOptions(mtgUserService.GetUser());
    }

    @RequestMapping("/getDeckTypeDropDownOptions")
    public String getDeckTypeDropDownOptions() {
        return mtgDeckService.GetDeckTypeDropDownOptions(mtgUserService.GetUser());
    }

    @RequestMapping("/getDeckList")
    public MTGDeckListDTO getDeckList(@RequestParam(value="decks-drop-down") String deckID) {
        return mtgDeckService.getDeckList(deckID);
    }

    @RequestMapping("/totalCollectionValue")
    public String totalCollectionValue() {
        return mtgCollectionAssetService.totalCollectionValue(mtgUserService.GetUser());
    }

    @RequestMapping("/getDeckCardDropDownOptions")
    public String getDeckCardDropDownOptions(@RequestParam(value="deck-drop-down") long deckID) {
        return mtgDeckAssetService.getDeckCardDropDownOptions(deckID);
    }

    @RequestMapping("/updateCardValues")
    public String updateCardValues(){
        log.info("Launching Update Card Value Process...");
        updateCardValues.updateCardValues();
        return "Card Values Updated";
    }
}
