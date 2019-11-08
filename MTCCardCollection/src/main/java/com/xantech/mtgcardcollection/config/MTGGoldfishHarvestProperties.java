package com.xantech.mtgcardcollection.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.xantech.mtgcardcollection.mtggoldfish")
@Data
public class MTGGoldfishHarvestProperties {
    private int throttelingPause;
}