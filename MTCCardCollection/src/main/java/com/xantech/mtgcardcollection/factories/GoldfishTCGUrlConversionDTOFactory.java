package com.xantech.mtgcardcollection.factories;

import com.xantech.mtgcardcollection.dto.GoldfishTCGUrlConversionDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GoldfishTCGUrlConversionDTOFactory {
    public GoldfishTCGUrlConversionDTO create(){
        GoldfishTCGUrlConversionDTO goldfishTCGUrlConversionDTO = new GoldfishTCGUrlConversionDTO();
        goldfishTCGUrlConversionDTO.setConversionFailures(new ArrayList<>());
        return goldfishTCGUrlConversionDTO;
    }
}
