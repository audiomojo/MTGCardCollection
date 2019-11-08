package com.xantech.mtgcardcollection.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.xantech.mtgcardcollection.schedules")
@Data
public class ScheduleProperties {
    private int updateCardValueInterval;
    private int getInitialDelay;
}