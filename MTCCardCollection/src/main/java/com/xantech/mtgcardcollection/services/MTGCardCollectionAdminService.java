package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCardRepository;
import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.factories.GoldfishTCGUrlConversionDTOFactory;
import com.xantech.mtgcardcollection.helpers.ScreenScrapeCardValueTCGPlayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGCardCollectionAdminService {
    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    GoldfishTCGUrlConversionDTOFactory goldfishTCGUrlConversionDTOFactory;

    @Autowired
    ScreenScrapeCardValueTCGPlayer screenScrapeCardValueTCGPlayer;

    public GoldfishTCGUrlConversionDTO generateAndValidateTCGURLs() {
        GoldfishTCGUrlConversionDTO goldfishTCGUrlConversionDTO = goldfishTCGUrlConversionDTOFactory.create();
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        goldfishTCGUrlConversionDTO.setCollectionCardCount(mtgCardList.size());
        goldfishTCGUrlConversionDTO.setConversionSuccessCount((int)mtgCardList.stream().filter(mtgCard -> convertAndValidate(mtgCard, goldfishTCGUrlConversionDTO) == true).count());
        goldfishTCGUrlConversionDTO.setConversionFailueCount(goldfishTCGUrlConversionDTO.getCollectionCardCount()-goldfishTCGUrlConversionDTO.getConversionSuccessCount());
        return goldfishTCGUrlConversionDTO;
    }

    private boolean convertAndValidate(MTGCard mtgCard, GoldfishTCGUrlConversionDTO goldfishTCGUrlConversionDTO) {
        boolean result = true;

        String url = buildTCGPlayerURL(mtgCard);

        String price = screenScrapeCardValueTCGPlayer.getPrice(url, "");

        if (price.compareTo("0.0") == 0){
            result = false;
        }

        return result;
    }

    private String buildTCGPlayerURL(MTGCard mtgCard) {
        String url = "https://shop.tcgplayer.com/magic/" + mtgCard.getBlock().replace('+', '-') + "/" + mtgCard.getCard().replace('+', ' ');
        String[] parts = url.split(" ");
        StringBuilder builder = new StringBuilder();
        Arrays.stream(parts).forEach(part -> { builder.append(part); builder.append("%20");});
        return builder.toString().substring(0, builder.toString().length()-3);
    }
}
