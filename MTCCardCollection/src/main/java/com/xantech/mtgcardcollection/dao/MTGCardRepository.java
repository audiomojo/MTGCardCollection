package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MTGCardRepository extends JpaRepository<MTGCard, Long> {
    MTGCard  findDistinctByMtgGoldfishURL(String url);
}
