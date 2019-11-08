package com.xantech.mtgcardcollection.launchers;

import com.xantech.mtgcardcollection.config.ScheduleProperties;
import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.services.MTGCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UpdateCardValues {
    @Autowired
    ScheduleProperties scheduleProperties;

    @Autowired
    MTGCardService mtgCardService;

    @Scheduled(fixedRateString = "${com.xantech.mtgcardcollection.schedules.updateCardValueInterval}", initialDelayString = "${com.xantech.mtgcardcollection.schedules.getInitialDelay}")
    public List<MTGCard> updateCardValues(){
        log.info(" *** <Executing Scheduled Task> *** : Updating Card Collection Values");
        return mtgCardService.updateCardValues();
    }
}
