package com.xantech.mtgcardcollection.view.ui;

public class LookupCard {

    private LookupCard(){};

    public static String HTML()
    {
        return  "<html>\n" +
                "<head>\n" +
                "  <title>MTG Card Details</title>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "  <form name=\"mtgcard\" action=\"parseCard\">\n" +
                "    MTG Goldfish Card URL:\n" +
                "    <br>\n" +
                "    <input type=\"hidden\" name=\"action\" value=\"LOOKUP\">\n" +
                "    <input type=\"hidden\" name=\"quantity\" value=\"0\">\n" +
                "    <input type=\"text\" name=\"url\" value=\"\" style=\"width:1000px;\">\n" +
                "    <br><br><br>\n" +
                "    <input type=\"submit\" value=\"AddCard Card to Collection\">\n" +
                "  </form>\n" +
                "</body>\n" +
                "</html>";
    }

}
