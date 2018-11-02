package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MTGDeckTypeRepository extends JpaRepository<MTGDeckType, Long> {
    List<MTGDeckType> findAllBy();
    List<MTGDeckType> findAllByUserID(Long id);
}
