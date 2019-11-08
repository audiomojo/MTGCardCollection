package com.xantech.mtgcardcollection.view.reports;

import com.xantech.mtgcardcollection.data.collections.CardCollection;
import com.xantech.mtgcardcollection.data.objects.Card;
import com.xantech.mtgcardcollection.data.objects.CardViewModel;
import com.xantech.mtgcardcollection.helpers.TextFormatting;

import java.util.Date;
import java.util.List;

public class CardValueSummary {
    private String format;

    public CardValueSummary(String format)
    {
        this.format = format;
    }

    public String CardValueSummaryReportHTML(List<CardViewModel> cardViewModelList)
    {
        StringBuilder cardValueSummaryReportHTML = new StringBuilder();
        double totalCollectionValue = 0.0;

        cardValueSummaryReportHTML.append("<html>");
        cardValueSummaryReportHTML.append("<head>");
        cardValueSummaryReportHTML.append("<title>MTG Card Collection Summary Report</title>");
        cardValueSummaryReportHTML.append("</head>");
        cardValueSummaryReportHTML.append("<body>");
        cardValueSummaryReportHTML.append("<h1>MTG Card Collection Summary Report</h1>");
        cardValueSummaryReportHTML.append(new Date().toString() + "<br><br>");
        for (CardViewModel cardViewModel : cardViewModelList) {
            cardValueSummaryReportHTML.append("URL: <a href=" + cardViewModel.getUrl() + ">URL</a><br>");
            cardValueSummaryReportHTML.append("Image URL: <a href=" + cardViewModel.getImageUrl() + ">Image URL</a><br>");
            cardValueSummaryReportHTML.append("Card: " + cardViewModel.getCard() + "<br>");
            cardValueSummaryReportHTML.append("Block: " + cardViewModel.getBlock() + "<br>");
            cardValueSummaryReportHTML.append("Format: " + cardViewModel.getFormat() + "<br>");
            cardValueSummaryReportHTML.append("Quantity: " + cardViewModel.getQuantity() + "<br>");
            double cardValue = cardViewModel.getValue();
            int cardCount = new Integer(cardViewModel.getQuantity());
            totalCollectionValue += (cardValue*cardCount);
            cardValueSummaryReportHTML.append("Value: $" + cardValue + "<br>");
            cardValueSummaryReportHTML.append("24-Hour Value Shift: " + TextFormatting.FormatAsUSD(cardViewModel.getTwentyFourHourValueShift()) + "<br>");
            cardValueSummaryReportHTML.append("24-Hour % Shift: " + TextFormatting.FormatAsPercentage(cardViewModel.getTwentyFourHourPercentageShift()) + "<br>");
            cardValueSummaryReportHTML.append("7-Day Value Shift: " + TextFormatting.FormatAsUSD(cardViewModel.getSevenDayValueShift()) + "<br>");
            cardValueSummaryReportHTML.append("7-Day % Shift: " + TextFormatting.FormatAsPercentage(cardViewModel.getSevenDayPercentageShift()) + "<br>");
            cardValueSummaryReportHTML.append("30-Day Value Shift: " + TextFormatting.FormatAsUSD(cardViewModel.getThirtyDayValueShift()) + "<br>");
            cardValueSummaryReportHTML.append("30-Day % Shift: " + TextFormatting.FormatAsPercentage(cardViewModel.getThirtyDayPercentageShift()) + "<br>");
            cardValueSummaryReportHTML.append("All History Value Shift: " + TextFormatting.FormatAsUSD(cardViewModel.getAllTimeValueShift()) + "<br>");
            cardValueSummaryReportHTML.append("All History % Shift: " + TextFormatting.FormatAsPercentage(cardViewModel.getAllTimePercentageShift()) + "<br>");
            cardValueSummaryReportHTML.append("Collection Value: " + TextFormatting.FormatAsUSD(cardValue * cardCount) + "<br>");
            cardValueSummaryReportHTML.append("<br><br>");
        }
        cardValueSummaryReportHTML.append("<h1>Total Collection Value: " + TextFormatting.FormatAsUSD(totalCollectionValue) + "</h1>");
        cardValueSummaryReportHTML.append("</body>");
        cardValueSummaryReportHTML.append("</html>");

        return cardValueSummaryReportHTML.toString();
    }
}
