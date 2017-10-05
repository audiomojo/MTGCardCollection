package com.xantech.mtgcardcollection.data.objects;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ValueNode implements Serializable {
    //private static final long serialVersionUID = 3454589924946773113L;
    private static final long serialVersionUID = 726528664611708250L;
    private Date date;
    private Double value;

    public ValueNode(Date date, Double value)
    {
        this.date = date;
        this.value = value;
    }
}
