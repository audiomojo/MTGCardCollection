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

    public boolean HasValueEntryForDate(Date date, ValueNode nodeCandidate)
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
        double response = 0.0;
        if(history.size() >= 2)
        {
            ValueNode mostRecentNode = history.get(history.size()-1);
            ValueNode priorNode = history.get(history.size()-2);
            response = mostRecentNode.getValue() - priorNode.getValue();
        }

        return response;
    }

    public double Get24HourPercentageShift() {
        double response = 0.0;
        if(history.size() >= 2)
        {
            ValueNode mostRecentNode = history.get(history.size()-1);
            ValueNode priorNode = history.get(history.size()-2);
            response = mostRecentNode.getValue()/priorNode.getValue()-1;
        }

        return response;
    }
}
