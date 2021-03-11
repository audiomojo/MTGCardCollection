package com.xantech.mtgcardcollection.controllers;

import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.dto.MTGCardPurgeResponseDTO;
import com.xantech.mtgcardcollection.dto.MTGGoldfishURLUpdateDTO;
import com.xantech.mtgcardcollection.services.MTGCardCollectionAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
