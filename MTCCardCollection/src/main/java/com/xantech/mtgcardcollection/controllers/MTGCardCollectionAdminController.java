package com.xantech.mtgcardcollection.controllers;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.dto.MTGCardPurgeResponseDTO;
import com.xantech.mtgcardcollection.dto.MTGGoldfishURLUpdateDTO;
import com.xantech.mtgcardcollection.services.MTGCardCollectionAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MTGCardCollectionAdminController {
    @Autowired
    MTGCardCollectionAdminService mtgCardCollectionAdminService;

    @RequestMapping("/generateAndValidateTCGURLs")
    public GoldfishTCGUrlConversionDTO generateAndValidateTCGURLs(){
        return  mtgCardCollectionAdminService.generateAndValidateTCGURLs();
    }

    @RequestMapping("/purgeCard")
    public MTGCardPurgeResponseDTO purgeCard(@RequestParam(value="cardID") long cardID){
        return  mtgCardCollectionAdminService.purgeCard(cardID);
    }


    @RequestMapping("/updateMTGGoldfishURL")
    public MTGGoldfishURLUpdateDTO updateMTGGoldfishURL(@RequestParam(value="cardID") long cardID,
                                                        @RequestParam(value="mtgURL") String mtgURL){
        return  mtgCardCollectionAdminService.updateMTGGoldfishURL(cardID, mtgURL);
    }

    @RequestMapping("/fixPromoURLs")
    public String fixPromoURLs(){
        return  mtgCardCollectionAdminService.fixPromoURLs();
    }

    @RequestMapping("/verifyMTGGoldfishURLs")
    public List<MTGCard> verifyMTGGoldfishURLs(){
        return  mtgCardCollectionAdminService.verifyMTGGoldfishURLs();
    }
}
