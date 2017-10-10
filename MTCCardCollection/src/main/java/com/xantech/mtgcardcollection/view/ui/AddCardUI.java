package com.xantech.mtgcardcollection.view.ui;

public class AddCardUI {

    private AddCardUI(){}

    public static String HTML()
    {
        return  "<html>\n" +
                "<head>\n" +
                "  <title>MTG Card Collection: AddCard Card</title>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "  <form name=\"mtgcard\" action=\"parseCard\">\n" +
                "    MTG Goldfish Card URL:\n" +
                "    <br>\n" +
                "    <input type=\"hidden\" name=\"action\" value=\"ADD\">\n" +
                "    <input type=\"text\" name=\"url\" value=\"\" style=\"width:1000px;\">\n" +
                "    <br><br><br>\n" +
                "    Quantity:\n" +
                "    <br>\n" +
                "    <input type=\"text\" name=\"quantity\" value=\"1\">\n" +
                "    <br>\n" +
                "    <br><br><br>\n" +
                "    <input type=\"submit\" value=\"Add Card to Collection\">\n" +
                "  </form>\n" +
                "</body>\n" +
                "</html>";
    }
}
