package com.xantech.mtgcardcollection.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MTGDeckType extends AbstractEntity {
    long userID;
    String deckType;
}
