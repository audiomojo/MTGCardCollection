package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MTGDeckRepository extends JpaRepository<MTGDeck, Long> {
    MTGDeck findTopByNameAndUserID(String name, long userID);
    List<MTGDeck> findAllByUserID(Long userID);
    MTGDeck findTopById(long ID);
}
