package com.xantech.mtgcardcollection.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MTGDeck extends AbstractEntity {
    long userID;
    String name;
    @Lob
    private String notes;
}
