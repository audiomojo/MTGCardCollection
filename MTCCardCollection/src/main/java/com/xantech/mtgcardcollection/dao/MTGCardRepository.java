package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MTGCardRepository extends JpaRepository<MTGCard, Long> {
    MTGCard findDistinctByMtgGoldfishURL(String url);
    List<MTGCard> findAllBy();
    MTGCard findDistinctById(long ID);
}
