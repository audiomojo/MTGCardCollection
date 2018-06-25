package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTGCardValueHistoryRepository  extends JpaRepository<MTGCardValueHistory, Long> {
    List<MTGCardValueHistory> findAllByCardIDOrderByModifiedDateDesc(long cardID);
    MTGCardValueHistory findTopByCardIDOrderByModifiedDateDesc(long cardID);
}
