package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MTGDeckAssetRepository extends JpaRepository<MTGDeckAsset, Long> {
    MTGDeckAsset findTopByCardIDAndUserIDAndDeckID(long cardID, long userID, long deckID);
    List<MTGDeckAsset> findAllByCardIDAndUserID(long cardID, long userID);
    List<MTGDeckAsset> findAllByDeckID(long deckID);
}
