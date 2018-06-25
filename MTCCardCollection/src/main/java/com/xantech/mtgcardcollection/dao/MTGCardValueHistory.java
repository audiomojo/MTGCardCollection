package com.xantech.mtgcardcollection.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MTGCardValueHistory extends AbstractEntity{
    private long cardID;
    private Date date;
    private double value;
}
