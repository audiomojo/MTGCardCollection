package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTGCollectionAssetRepository  extends JpaRepository<MTGCollectionAsset, Long> {
    MTGCollectionAsset findTopByCardIDAndUserID(long cardID, long userID);
    List<MTGCollectionAsset> findAllByUserID(long userID);
}
