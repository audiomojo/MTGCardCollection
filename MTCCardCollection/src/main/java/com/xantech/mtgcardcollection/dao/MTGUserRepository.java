package com.xantech.mtgcardcollection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTGUserRepository extends JpaRepository<MTGUser, Long> {
    MTGUser findTopByUserName(String userName);
    MTGUser findTopBy();
}
