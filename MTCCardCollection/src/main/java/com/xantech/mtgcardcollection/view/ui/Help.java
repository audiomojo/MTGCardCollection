package com.xantech.mtgcardcollection.view.ui;

public class Help {

    private Help(){};

    public static String HTML()
    {
        return  "<html>\n" +
                "   <head>\n" +
                "       <title>MTG Card Collection: Help</title>\n" +
                "       <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "   </head>\n" +
                "   <body>\n" +
                "       MTG Card Collection: Help\n" +
                "       <br><br><br>\n" +
                "       <a href=\"/addCard\">Add Card...</a><br>\n" +
                "       <a href=\"/deleteCard\">Delete Card...</a><br>\n" +
                "       <a href=\"/decrementCardCount\">Decrement Card Count...</a><br>\n" +
                "       <a href=\"/lookupCard\">Lookup Card...</a>    <br>\n" +
                "       <br><br><br>\n" +
                "       <a href=\"/updateCollectionValues\">Update Collection Values...</a><br>\n" +
                "       <a href=\"/cardValueSummary?format=CARD\">Card Value Summary: Sort by Card Name...</a><br>\n" +
                "       <a href=\"/cardValueSummary?format=BLOCK-CARD\">Card Value Summary: Sort by Block, then Card...</a><br>\n" +
                "       <a href=\"/cardValueSummary?format=PRICE\">Card Value Summary: Sort by Price...</a><br>\n" +
                "       <a href=\"/cardValueSummary?format=VALUE-CHANGE\">Card Value Summary: Sort by Value Change...</a><br>\n" +
                "   </body>\n" +
                "</html>";
    }
}
