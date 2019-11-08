package com.xantech.mtgcardcollection.data.collections;

import com.xantech.mtgcardcollection.data.objects.Card;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@Data
public class CardCollection implements java.io.Serializable {
    private ArrayList<Card> collection = null;

    public CardCollection() {
        CardCollection cardCollection = LoadCollection();
        if (cardCollection == null) {
            collection = new ArrayList<>();
        } else {
            collection = cardCollection.collection;
        }
    }

    private static CardCollection LoadCollection() {
        CardCollection cardCollection = null;

        try {
            FileInputStream fileInStream = new FileInputStream("myObjectFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fileInStream);
            cardCollection = (CardCollection) ois.readObject();
            ois.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception: " + ex.toString());
        } catch (IOException ex) {
            System.out.println("IO Exception: " + ex.toString());
        } catch (ClassNotFoundException ex) {
            System.out.println("Class Not Found Exception: " + ex.toString());
        }

        return cardCollection;
    }
}
