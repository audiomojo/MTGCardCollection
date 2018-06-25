package com.xantech.mtgcardcollection.data.collections;

import com.xantech.mtgcardcollection.data.objects.ValueNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Data
public class ValueHistory implements Serializable {
    private static final long serialVersionUID = 726528664611708250L;
    private ArrayList<ValueNode> history = null;

    public ValueHistory()
    {
        history = new ArrayList<>();
    }

    private boolean HasValueEntryForDate(Date date, ValueNode nodeCandidate)
    {
        boolean result = false;

        for (ValueNode node : history) {
            if (SameDay(node.getDate())) {
                node.setDate(nodeCandidate.getDate());
                node.setValue(nodeCandidate.getValue());
                result = true;
            }
        }
        return result;
    }

    private boolean SameDay(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        calendarDate.setTime(date);
        today.setTime(new Date());
        return calendarDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendarDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendarDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    public void AddNode(ValueNode node)
    {
        if (!HasValueEntryForDate(new Date(), node))
            history.add(node);
    }

    public ValueNode GetTodaysValue() {
        for (ValueNode valueNode : history) {
            if (SameDay(valueNode.getDate()))
                return valueNode;
        }
        return null;
    }

    public double Get24HourValueShift() {
        return CalculateValueShift(1);
    }

    public double Get24HourPercentageShift() {
        return CalculatePercentageShift(1);
    }

    public double Get7DayValueShift() {
        return CalculateValueShift(7);
    }

    public double Get7DayPercentageShift() {
        return CalculatePercentageShift(7);
    }

    public double Get30DayValueShift() {
        return CalculateValueShift(30);
    }

    public double Get30DayPercentageShift() {
        return CalculatePercentageShift(30);
    }

    public double GetAllTimeValueShift() {
        ValueNode mostRecentNode = history.get(history.size()-1);
        ValueNode priorNode = history.get(0);
        return mostRecentNode.getValue() - priorNode.getValue();
    }

    public double GetAllTimePercentageShift()
    {
        ValueNode mostRecentNode = history.get(history.size()-1);
        ValueNode priorNode = history.get(0);
        return mostRecentNode.getValue()/priorNode.getValue()-1;
    }

    private double CalculatePercentageShift(int days){
        double response = 0.0;
        if(history.size() >= days+1)
        {
            ValueNode mostRecentNode = history.get(history.size()-1);
            ValueNode priorNode = history.get(history.size()-(days+1));
            response = mostRecentNode.getValue()/priorNode.getValue()-1;
        }

        return response;
    }

    private double CalculateValueShift(int days){
        double response = 0.0;
        if(history.size() >= days+1)
        {
            ValueNode mostRecentNode = history.get(history.size()-1);
            ValueNode priorNode = history.get(history.size()-(days+1));
            response = mostRecentNode.getValue() - priorNode.getValue();
        }

        return response;
    }
}
