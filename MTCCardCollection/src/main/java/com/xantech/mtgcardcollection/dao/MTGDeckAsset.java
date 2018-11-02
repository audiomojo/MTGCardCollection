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
public class MTGDeckAsset extends AbstractEntity {
    long cardID;
    long userID;
    long deckID;
    int quantity;
    @Lob
    private String transactionHistory;
}
