package com.xantech.mtgcardcollection.controllers;

import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.services.MTGCardCollectionAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MTGCardCollectionAdminController {
    @Autowired
    MTGCardCollectionAdminService mtgCardCollectionAdminService;

    @RequestMapping("/generateAndValidateTCGURLs")
    public GoldfishTCGUrlConversionDTO generateAndValidateTCGURLs(){
        return  mtgCardCollectionAdminService.generateAndValidateTCGURLs();
    }

}
