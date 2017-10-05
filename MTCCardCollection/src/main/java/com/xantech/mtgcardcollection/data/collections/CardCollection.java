package com.xantech.mtgcardcollection.data.collections;

import com.xantech.mtgcardcollection.data.objects.Card;
import lombok.Data;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@Data
public class CardCollection implements java.io.Serializable {
    private static final long serialVersionUID = 726528664611708250L;
    private int identity;
    private Date lastValueCheck;
    private ArrayList<Card> collection = null;

    public CardCollection()
    {
        CardCollection cardCollection = LoadCollection();
        if (cardCollection == null) {
            identity = 0;
            collection = new ArrayList<>();
        }
        else {
            identity = cardCollection.identity;
            lastValueCheck = cardCollection.lastValueCheck;
            collection = cardCollection.collection;
        }
    }

    public boolean AddCard(Card card) {
        boolean result = false;

        try {
            Card existingCard = GetCard(card);
            if (existingCard == null) {
                collection.add(card);
                card.setId(identity+=1);
                card.MeasureValue();
            }
            else {
                existingCard.setQuantity(existingCard.getQuantity() + card.getQuantity());
            }
            result = SaveCollection();
        }
        catch (Exception ex)
        {
            System.out.println("Error: " + ex.toString());
        }

        return result;
    }

    private Card GetCard(Card card) {
        for (Card currentCard : collection) {
            if(currentCard.getBlock().compareTo(card.getBlock()) == 0 &&
                currentCard.getCard().compareTo(card.getCard()) == 0 &&
                currentCard.getFormat().compareTo(card.getFormat()) == 0)
            return currentCard;
        }
        return null;
    }

    public static CardCollection LoadCollection() {
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

    private boolean SaveCollection() {
        boolean result = false;
        try {
            FileOutputStream fileStream = new FileOutputStream("myObjectFile.txt");
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            //String output = collection.toString();
            os.writeObject(this);
            os.close();
            result = true;
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File Not Found Exception: " + ex.toString());
        }
        catch (IOException ex)
        {
            System.out.println("IO Exception: " + ex.toString());
        }

        return  result;
    }

//    public ArrayList<Card> GetReadOnlyCollection()
//    {
//        ArrayList<Card> collectionCopy = new ArrayList<>();
//
//        for (Card card : collection) {
//            collectionCopy.add(card.Clone());
//        }
//
//        return collectionCopy;
//    }

    public String UpdateCollectionValues() {
        Date checkTime = new Date();

        if ((lastValueCheck == null) || (checkTime.getTime() - lastValueCheck.getTime() > 300000)) {
            for (Card card : collection) {
                card.MeasureValue();
            }
            lastValueCheck = checkTime;
            SaveCollection();
        }
        return collection.toString();
    }


    public String DebugCardCollection() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head>");
        stringBuilder.append("<title>MTG Card Collection DEBUG Report</title>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");
        stringBuilder.append("<h1>MTG Card Collection DEBUG Report</h1>");
        stringBuilder.append(new Date().toString() + "<br><br>");
        for (Card card : collection) {
            stringBuilder.append("Block: " + card.getBlock() + ";  Card: " + card.getCard() + ";  SortCollection: " + card.getFormat() + ";  Count: " + card.getQuantity() + "; <br>");
        }
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }

    public Card DeleteCard(String block, String cardName, String format) {
        Card result = LookupCard(block, cardName, format);

        if (result != null) {
            System.out.println("Pre Deletion...  Collection Size: " + collection.size());
            boolean success = collection.remove(result);
            System.out.println("Post Deletion...  Collection Size: " + collection.size());
            if (success)
                SaveCollection();
        }
        return result;
    }

    public Card DecrementCard(String block, String cardName, String format, String count) {
        Card result = null;

        for (Card card : collection) {
            if ((card.getBlock().compareTo(block) == 0) && (card.getCard().compareTo(cardName) == 0) && (card.getFormat().compareTo(format) == 0)) {
                result = card;
                break;
            }
        }

        if (result != null) {
            System.out.println("Pre Decrement...  Collection Size: " + collection.size());
            if (result.getQuantity() <= Integer.parseInt(count))
                collection.remove(result);
            else
                result.setQuantity(result.getQuantity() - Integer.parseInt(count));
            System.out.println("Post Decrement...  Collection Size: " + collection.size());
            SaveCollection();
        }
        return result;
    }

    public void SortCollection(String format){
        if (format.compareTo("BLOCK-CARD") == 0)
            SortBlockCard();
        else if (format.compareTo("PRICE") == 0)
            SortPrice();
        else if (format.compareTo("VALUE-CHANGE") == 0)
            SortValueChange();
        else {
            SortCard();
            SaveCollection();
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

    private void SortValueChange() {
        Collections.sort(collection, new SortByValueChange());
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

class SortByValueChange implements Comparator<Card>
{
    public int compare(Card card1, Card card2)
    {
        double sortValue = card2.getValueHistory().Get24HourValueShift()*100 - card1.getValueHistory().Get24HourValueShift()*100;
        return (int) sortValue;
    }
}

