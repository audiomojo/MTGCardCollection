package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGUser;
import com.xantech.mtgcardcollection.dao.MTGUserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@Data
public class MTGUserService {

    @Autowired
    MTGUserRepository mtgUserRepository;

    public MTGUser AddUser(String userName, String email, String mobile, String password) {
        Date date = new Date();
        MTGUser mtgUser = mtgUserRepository.findTopByUserName(userName);

        if (mtgUser == null) {
            mtgUser = new MTGUser();
            mtgUser.setCreatedDate(date);
            mtgUser.setUserName(userName);
        }
        mtgUser.setModifiedDate(date);
        mtgUser.setEmailAddress(email);
        mtgUser.setMobileNumber(mobile);
        mtgUser.setPassword(password);
        mtgUserRepository.save(mtgUser);
        return mtgUser;
    }

    public MTGUser GetUser(){
        return mtgUserRepository.findTopBy();
    }
}
