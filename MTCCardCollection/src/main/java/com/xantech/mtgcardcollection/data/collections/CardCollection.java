package com.xantech.mtgcardcollection.data.collections;

import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.data.objects.CardViewModel;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@Data
public class CardCollection implements java.io.Serializable {
    private ArrayList<Card> collection = null;

    public CardCollection()
    {
        CardCollection cardCollection = LoadCollection();
        if (cardCollection == null) {
            collection = new ArrayList<>();
        }
        else {
            collection = cardCollection.collection;
        }
    }

    private static CardCollection LoadCollection() {
        CardCollection cardCollection = null;

        try{
            FileInputStream fileInStream = new FileInputStream("myObjectFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fileInStream);
            cardCollection = (CardCollection)ois.readObject();
            ois.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File Not Found Exception: " + ex.toString());
        }
        catch (IOException ex)
        {
            System.out.println("IO Exception: " + ex.toString());
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("Class Not Found Exception: " + ex.toString());
        }

        return  cardCollection;
    }

    public String UpdateCollectionValues(String override) {
        Date checkTime = new Date();

        // 1 hour = 3600000
        // 3 hours = 10800000
        // 6 hours = 21600000

        int index = 0;
//        if ((lastValueCheck == null) || (override.compareTo("TRUE") == 0) || (checkTime.getTime() - lastValueCheck.getTime() > 10800000)) {
//            collectionValue = 0.0;
//            for (Card card : collection) {
//                System.out.println(index++ + " of " + collection.size() + " : " + card.toString());
//                card.MeasureValue();
//                collectionValue += card.GetCardValue()*card.getQuantity();
//                try {
//                    Thread.sleep(250);
//                } catch (InterruptedException ex) {
//                    System.out.println("Thread Exception: " + ex.toString());
//                }
//                lastValueCheck = checkTime;
//                SaveCollection();
//            }
//        }
        return collection.toString();
    }

//    public String DebugCardCollection() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("<html>");
//        stringBuilder.append("<head>");
//        stringBuilder.append("<title>MTG Card Collection DEBUG Report</title>");
//        stringBuilder.append("</head>");
//        stringBuilder.append("<body>");
//        stringBuilder.append("<h1>MTG Card Collection DEBUG Report</h1>");
//        stringBuilder.append(new Date().toString() + "<br><br>");
//        for (Card card : collection) {
//            stringBuilder.append("Block: " + card.getBlock() + ";  Card: " + card.getCard() + ";  SortCollection: " + card.getFormat() + ";  Count: " + card.getQuantity() + "; <br>");
//        }
//        stringBuilder.append("</body>");
//        stringBuilder.append("</html>");
//        return stringBuilder.toString();
//    }

//    public ArrayList<CardViewModel> GetModel(String format, String override) {
//        UpdateCollectionValues(override);
//        SortCollection(format);
//        return GetModelCollection();
//    }
//
//    private ArrayList<CardViewModel> GetModelCollection() {
//        ArrayList<CardViewModel> cardViewModels = new ArrayList<>();
//        for (Card card : collection) {
//            cardViewModels.add(new CardViewModel(card));
//        }
//        return cardViewModels;
//    }
//
    public void SortCollection(String format){
        if (format.compareTo("BLOCK-CARD") == 0)
            SortBlockCard();
        else if (format.compareTo("PRICE") == 0)
            SortPrice();
        else if (format.compareTo("DAY-VALUE-CHANGE") == 0)
            SortDayValueChange();
        else if (format.compareTo("7-DAY-VALUE-CHANGE") == 0)
            Sort7DayValueChange();
        else if (format.compareTo("30-DAY-VALUE-CHANGE") == 0)
            Sort30DayValueChange();
        else if (format.compareTo("ALL-TIME-VALUE-CHANGE") == 0)
            SortAllTimeValueChange();
        else {
            SortCard();
            //SaveCollection();
        }
    }

    private void SortCard() {
        Collections.sort(collection, new SortByCard());
    }

    private void SortBlockCard() {
        Collections.sort(collection, new SortByBlockCard());
    }

    private void SortPrice() {
        Collections.sort(collection, new SortByPrice());
    }

    private void SortDayValueChange() {
        Collections.sort(collection, new SortByDayValueChange());
    }

    private void Sort7DayValueChange() {
        Collections.sort(collection, new SortBy7DayValueChange());
    }

    private void Sort30DayValueChange() {
        Collections.sort(collection, new SortBy30DayValueChange());
    }

    private void SortAllTimeValueChange() {
        Collections.sort(collection, new SortByAllTimeValueChange());
    }

    public Card LookupCard(String block, String cardName, String format) {
        Card result = null;

        for (Card card : collection) {
            if ((card.getBlock().compareTo(block) == 0) && (card.getCard().compareTo(cardName) == 0) && (card.getFormat().compareTo(format) == 0)) {
                result = card;
                break;
            }
        }
        return result;
    }
}

class SortByBlockCard implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        if(card1.getBlock().compareTo(card2.getBlock()) == 0)
            return card1.getCard().compareTo(card2.getCard());
        else
            return card1.getBlock().compareTo(card2.getBlock());
    }
}

class SortByCard implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        return card1.getCard().compareTo(card2.getCard());
    }
}

class SortByPrice implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
            double sortValue = card1.getValueHistory().GetTodaysValue().getValue()*100 - card2.getValueHistory().GetTodaysValue().getValue()*100;
        return (int) sortValue*-1;
    }
}

class SortByDayValueChange implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        double sortValue = card2.getValueHistory().Get24HourValueShift()*100 - card1.getValueHistory().Get24HourValueShift()*100;
        return (int) sortValue;
    }
}

class SortBy7DayValueChange implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        double sortValue = card2.getValueHistory().Get7DayValueShift()*100 - card1.getValueHistory().Get7DayValueShift()*100;
        return (int) sortValue;
    }
}

class SortBy30DayValueChange implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        double sortValue = card2.getValueHistory().Get30DayValueShift()*100 - card1.getValueHistory().Get30DayValueShift()*100;
        return (int) sortValue;
    }
}

class SortByAllTimeValueChange implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        double sortValue = card2.getValueHistory().GetAllTimeValueShift()*100 - card1.getValueHistory().GetAllTimeValueShift()*100;
        return (int) sortValue;
    }
}

