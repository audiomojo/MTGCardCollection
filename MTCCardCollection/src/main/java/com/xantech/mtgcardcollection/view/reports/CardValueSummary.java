package com.xantech.mtgcardcollection.view.reports;

import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.helpers.TextFormatting;

import java.util.Date;

public class CardValueSummary {
    private String format;

    public CardValueSummary(String format)
    {
        this.format = format;
    }

    public String CardValueSummaryReportHTML()
    {
        StringBuilder cardValueSummaryReportHTML = new StringBuilder();
        CardCollection cardCollection = new CardCollection();
        cardCollection.UpdateCollectionValues();
        cardCollection.SortCollection(format);
        double totalCollectionValue = 0.0;


        cardValueSummaryReportHTML.append("<html>");
        cardValueSummaryReportHTML.append("<head>");
        cardValueSummaryReportHTML.append("<title>MTG Card Collection Summary Report</title>");
        cardValueSummaryReportHTML.append("</head>");
        cardValueSummaryReportHTML.append("<body>");
        cardValueSummaryReportHTML.append("<h1>MTG Card Collection Summary Report</h1>");
        cardValueSummaryReportHTML.append(new Date().toString() + "<br><br>");
        for (Card card : cardCollection.getCollection()) {
            cardValueSummaryReportHTML.append("Card: " + card.getCard() + "<br>");
            cardValueSummaryReportHTML.append("Block: " + card.getBlock() + "<br>");
            cardValueSummaryReportHTML.append("Format: " + card.getFormat() + "<br>");
            cardValueSummaryReportHTML.append("Quantity: " + card.getQuantity() + "<br>");
            double cardValue = card.GetCardValue();
            totalCollectionValue += (cardValue*card.getQuantity());
            cardValueSummaryReportHTML.append("Value: $" + cardValue + "<br>");
            cardValueSummaryReportHTML.append("24-Hour Value Shift: " + TextFormatting.FormatAsUSD(card.getCardValueMetrics().getTwentyFourHourValueShift()) + "<br>");
            cardValueSummaryReportHTML.append("24-Hour % Shift: " + TextFormatting.FormatAsPercentage(card.getCardValueMetrics().getTwentyFourHourPercentageShift()) + "<br>");
            cardValueSummaryReportHTML.append("Collection Value: " + TextFormatting.FormatAsUSD(cardValue * card.getQuantity()) + "<br>");
            cardValueSummaryReportHTML.append("<br><br>");
        }
        cardValueSummaryReportHTML.append("<h1>Total Collection Value: " + TextFormatting.FormatAsUSD(totalCollectionValue) + "</h1>");
        cardValueSummaryReportHTML.append("</body>");
        cardValueSummaryReportHTML.append("</html>");

        return cardValueSummaryReportHTML.toString();
    }
}
