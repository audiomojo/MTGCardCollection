package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import com.xantech.mtgcardcollection.dto.MTGCardPurgeResponseDTO;
import com.xantech.mtgcardcollection.dto.MTGGoldfishURLUpdateDTO;
import com.xantech.mtgcardcollection.factories.GoldfishTCGUrlConversionDTOFactory;
import com.xantech.mtgcardcollection.helpers.ScreenScrapeCardValueTCGPlayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Data
public class MTGCardCollectionAdminService {
    @Autowired
    MTGCardRepository mtgCardRepository;

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    @Autowired
    MTGCollectionAssetRepository mtgCollectionAssetRepository;

    @Autowired
    GoldfishTCGUrlConversionDTOFactory goldfishTCGUrlConversionDTOFactory;

    @Autowired
    ScreenScrapeCardValueTCGPlayer screenScrapeCardValueTCGPlayer;

    @Autowired
    MTGUserService mtgUserService;

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

    public MTGCardPurgeResponseDTO purgeCard(long cardID) {
        MTGCardPurgeResponseDTO mtgCardPurgeResponseDTO = new MTGCardPurgeResponseDTO();
        mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(true);

        MTGCollectionAsset mtgCollectionAsset = mtgCollectionAssetRepository.findTopByCardIDAndUserID(cardID, mtgUserService.GetUser().getId());
        if (mtgCollectionAsset != null) {
            mtgCollectionAssetRepository.delete(mtgCollectionAsset);
            mtgCardPurgeResponseDTO.setPurgeCollectionAssetSuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCollectionAssetSuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Collection Asset for cardID: " + cardID + " resulted in a null value");
        }

        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryRepository.findAllByCardIDOrderByModifiedDateDesc(cardID);
        if (mtgCardValueHistoryList != null) {
            mtgCardPurgeResponseDTO.setCardValueHistorySize(mtgCardValueHistoryList.size());
            mtgCardValueHistoryList.stream().forEach(mtgCardValueHistory -> mtgCardValueHistoryRepository.delete(mtgCardValueHistory));
            mtgCardPurgeResponseDTO.setPurgeCardValueHistorySuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCardValueHistorySuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Card Value Histories for cardID: " + cardID + " resulted in a null value");
        }

        MTGCard mtgCard = mtgCardRepository.findDistinctById(cardID);
        if (mtgCard != null) {
            mtgCardRepository.delete(mtgCard);
            mtgCardPurgeResponseDTO.setPurgeCardSuccess(true);
        } else {
            mtgCardPurgeResponseDTO.setPurgeCardSuccess(false);
            mtgCardPurgeResponseDTO.setOverallPurgeActionSuccess(false);
            mtgCardPurgeResponseDTO.setNote(mtgCardPurgeResponseDTO.getNote() + "\nThere was an error:  Requesting Card for cardID: " + cardID + " resulted in a null value");
        }

        return mtgCardPurgeResponseDTO;
    }

    public MTGGoldfishURLUpdateDTO updateMTGGoldfishURL(long cardID, String mtgURL) {
        MTGGoldfishURLUpdateDTO mtgGoldfishURLUpdateDTO = new MTGGoldfishURLUpdateDTO();
        MTGCard mtgCard = mtgCardRepository.findDistinctById(cardID);
        if (mtgCard != null) {
            mtgGoldfishURLUpdateDTO.setOldURL(mtgCard.getMtgGoldfishURL());
            mtgGoldfishURLUpdateDTO.setNewURL(mtgURL);
            mtgCard.setMtgGoldfishURL(mtgURL);
            mtgCardRepository.save(mtgCard);
        }

        return mtgGoldfishURLUpdateDTO;
    }

    public String fixPromoURLs() {
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        List<MTGCard> badURLList = getBadURLs(mtgCardList);
        return "Number of fixed URLs: " + badURLList.size();
    }

    private List<MTGCard> getBadURLs(List<MTGCard> mtgCardList) {
        List<MTGCard> badURLList = new ArrayList<>();

        mtgCardList.stream().forEach(mtgCard -> checkForPromoURL(mtgCard, badURLList));

        return badURLList;
    }

    private void checkForPromoURL(MTGCard mtgCard, List<MTGCard> badURLList) {
        Document document;
        if ((mtgCard.getBlock().contains("Promos") ||
                (mtgCard.getMtgGoldfishURL().contains("Promos")))) {
            try {
                System.out.println("Checking Card: " + mtgCard.getMtgGoldfishURL());
                document = Jsoup.connect(mtgCard.getMtgGoldfishURL()).get();
                if (document.text().contains("Throttled"))
                    System.out.println("Throttled: " + mtgCard.getMtgGoldfishURL());
            } catch (IOException ex1) {
                System.out.println("Error Getting Value for: " + mtgCard.getMtgGoldfishURL());
                mtgCard.setMtgGoldfishURL(mtgCard.getMtgGoldfishURL().replaceAll("\\+Promos", ""));

                try {
                    document = Jsoup.connect(mtgCard.getMtgGoldfishURL()).get();
                    if (document.text().contains("Throttled")) {
                        System.out.println("Throttled: " + mtgCard.getMtgGoldfishURL());
                    } else {
                        System.out.println("Success updating URL: " + mtgCard.getMtgGoldfishURL());
                        mtgCardRepository.save(mtgCard);
                        badURLList.add(mtgCard);
                    }
                } catch (IOException ex2) {
                    System.out.println("New URL has errors: " + mtgCard.getMtgGoldfishURL());
                }
            }
        }
    }

    public List<MTGCard> verifyMTGGoldfishURLs() {
        List<MTGCard> mtgCardList = mtgCardRepository.findAllBy();
        List<MTGCard> badURLList = new ArrayList<>();
        List<MTGCard> retryList = new ArrayList<>();

        mtgCardList.stream().forEach(mtgCard -> validateURL(mtgCard, badURLList, retryList));

        while (retryList.size() > 0) {
            mtgCardList.clear();
            retryList.stream().forEach(mtgCard -> mtgCardList.add(mtgCard));
            retryList.clear();
            mtgCardList.stream().forEach(mtgCard -> validateURL(mtgCard, badURLList, retryList));
        }

        return badURLList;
    }

    private void validateURL(MTGCard mtgCard, List<MTGCard> badURLList, List<MTGCard> retryList) {
        Document document;
        try {
            System.out.println("Checking Card: " + mtgCard.getMtgGoldfishURL());
            document = Jsoup.connect(mtgCard.getMtgGoldfishURL()).get();
            if (document.text().contains("Throttled")) {
                System.out.println("Throttled: " + mtgCard.getMtgGoldfishURL());
                retryList.add(mtgCard);
            }
        } catch (IOException ex1) {
            System.out.println("Error Getting Value for: " + mtgCard.getMtgGoldfishURL());
            badURLList.add(mtgCard);
        }
    }
}
