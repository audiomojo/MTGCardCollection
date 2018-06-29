package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.*;
import com.xantech.mtgcardcollection.data.objects.CardViewModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Data
public class MTGCollectionReportService {

    @Autowired
    MTGCardService mtgCardService;

    @Autowired
    MTGCollectionAssetRepository mtgCollectionAssetRepository;

    public List<CardViewModel> GetModel(String format, String override, MTGUser mtgUser) {
        List<MTGCard> mtgCardList = mtgCardService.UpdateCardValues(override);
        List<MTGCollectionAsset> mtgCollectionAssetList = mtgCollectionAssetRepository.findAllByUserID(mtgUser.getId());
        List<CardViewModel> cardViewModelList = GetModelCollection(mtgCardList, mtgCollectionAssetList);
        SortCollection(cardViewModelList, format);
        return cardViewModelList;
    }

    private ArrayList<CardViewModel> GetModelCollection(List<MTGCard> mtgCardList, List<MTGCollectionAsset> mtgCollectionAssetList) {
        ArrayList<CardViewModel> cardViewModelList = new ArrayList<>();
        for (MTGCollectionAsset mtgCollectionAsset : mtgCollectionAssetList) {
            MTGCard mtgCard = mtgCardList.stream().filter(card -> mtgCollectionAsset.getCardID() == card.getId()).collect(Collectors.toList()).get(0);
            cardViewModelList.add(new CardViewModel(mtgCollectionAsset, mtgCard));
        }
        return cardViewModelList;
    }

    public void SortCollection(List<CardViewModel> cardViewModelList, String format){
        if (format.compareTo("BLOCK-CARD") == 0)
            SortBlockCard(cardViewModelList);
        else if (format.compareTo("PRICE") == 0)
            SortPrice(cardViewModelList);
        else if (format.compareTo("DAY-VALUE-CHANGE") == 0)
            SortDayValueChange(cardViewModelList);
        else if (format.compareTo("7-DAY-VALUE-CHANGE") == 0)
            Sort7DayValueChange(cardViewModelList);
        else if (format.compareTo("30-DAY-VALUE-CHANGE") == 0)
            Sort30DayValueChange(cardViewModelList);
        else if (format.compareTo("ALL-TIME-VALUE-CHANGE") == 0)
            SortAllTimeValueChange(cardViewModelList);
        else {
            SortCard(cardViewModelList);
        }
    }

    private void SortCard(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortByCard());
    }

    private void SortBlockCard(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortByBlockCard());
    }

    private void SortPrice(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortByPrice());
    }

    private void SortDayValueChange(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortByDayValueChange());
    }

    private void Sort7DayValueChange(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortBy7DayValueChange());
    }

    private void Sort30DayValueChange(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortBy30DayValueChange());
    }

    private void SortAllTimeValueChange(List<CardViewModel> cardViewModelList) {
        Collections.sort(cardViewModelList, new SortByAllTimeValueChange());
    }

//    public Card LookupCard(String block, String cardName, String format) {
//        Card result = null;
//
//        for (Card card : collection) {
//            if ((card.getBlock().compareTo(block) == 0) && (card.getCard().compareTo(cardName) == 0) && (card.getFormat().compareTo(format) == 0)) {
//                result = card;
//                break;
//            }
//        }
//        return result;
//    }
}

class SortByCard implements Comparator<CardViewModel>{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        return card1.getCard().compareTo(card2.getCard());
    }
}

class SortByBlockCard implements Comparator<CardViewModel>{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        if(card1.getBlock().compareTo(card2.getBlock()) == 0)
            return card1.getCard().compareTo(card2.getCard());
        else
            return card1.getBlock().compareTo(card2.getBlock());
    }
}

class SortByPrice implements Comparator<CardViewModel>{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        double sortValue = card1.getValue()*100 - card2.getValue()*100;
        return (int) sortValue*-1;
    }
}

class SortByDayValueChange implements Comparator<CardViewModel>
{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        double sortValue = card2.getTwentyFourHourValueShift()*100 - card1.getTwentyFourHourValueShift()*100;
        return (int) sortValue;
    }
}

class SortBy7DayValueChange implements Comparator<CardViewModel>
{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        double sortValue = card2.getSevenDayValueShift()*100 - card1.getSevenDayValueShift()*100;
        return (int) sortValue;
    }
}

class SortBy30DayValueChange implements Comparator<CardViewModel>
{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        double sortValue = card2.getThirtyDayValueShift()*100 - card1.getThirtyDayValueShift()*100;
        return (int) sortValue;
    }
}

class SortByAllTimeValueChange implements Comparator<CardViewModel>
{
    public int compare(CardViewModel card1, CardViewModel card2)
    {
        double sortValue = card2.getAllTimeValueShift()*100 - card1.getAllTimeValueShift()*100;
        return (int) sortValue;
    }
}

