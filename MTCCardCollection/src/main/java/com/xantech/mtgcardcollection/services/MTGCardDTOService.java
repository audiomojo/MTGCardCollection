package com.xantech.mtgcardcollection.services;

import com.xantech.mtgcardcollection.dao.MTGCard;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistory;
import com.xantech.mtgcardcollection.dao.MTGCardValueHistoryRepository;
import com.xantech.mtgcardcollection.dto.MTGCardDTO;
import com.xantech.mtgcardcollection.dto.MTGCardValueHistoryDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class MTGCardDTOService {

    @Autowired
    MTGCardValueHistoryRepository mtgCardValueHistoryRepository;

    private MTGCardDTO mtgCardDTO;

    public MTGCardDTO AssembleMTGCardDTO(MTGCard mtgCard) {
        CopyMTGCard(mtgCard);

        CopyMTGCardValueHistoryList(mtgCard);

        return mtgCardDTO;
    }

    private void CopyMTGCardValueHistoryList(MTGCard mtgCard) {
        List<MTGCardValueHistory> mtgCardValueHistoryList = mtgCardValueHistoryRepository.findAllByCardIDOrderByModifiedDateDesc(mtgCard.getId());
        List<MTGCardValueHistoryDTO> mtgCardValueHistoryDTOList = new ArrayList<>();
        mtgCardDTO.setValueHistoryDTOList(mtgCardValueHistoryDTOList);
        for (MTGCardValueHistory mtgCardValueHistory : mtgCardValueHistoryList) {
            mtgCardValueHistoryDTOList.add(CopyMTGValueHistory(mtgCardValueHistory));
        }
    }

    private MTGCardValueHistoryDTO CopyMTGValueHistory(MTGCardValueHistory mtgCardValueHistory) {
        MTGCardValueHistoryDTO mtgCardValueHistoryDTO = new MTGCardValueHistoryDTO();

        mtgCardValueHistoryDTO.setValue(mtgCardValueHistory.getValue());
        mtgCardValueHistoryDTO.setDate(mtgCardValueHistory.getDate());
        mtgCardValueHistoryDTO.setDateString(mtgCardValueHistory.getDate().toString());

        return mtgCardValueHistoryDTO;
    }

    private void CopyMTGCard(MTGCard mtgCard) {
        mtgCardDTO = new MTGCardDTO();
        mtgCardDTO.setBlock(mtgCard.getBlock());
        mtgCardDTO.setCard(mtgCard.getCard());
        mtgCardDTO.setFormat(mtgCard.getFormat());
        mtgCardDTO.setQuantity(mtgCard.getQuantity());
        mtgCardDTO.setTwentyFourHourValueShift(mtgCard.getTwentyFourHourValueShift());
        mtgCardDTO.setTwentyFourHourPercentageShift(mtgCard.getTwentyFourHourPercentageShift());
        mtgCardDTO.setSevenDayValueShift(mtgCard.getSevenDayValueShift());
        mtgCardDTO.setSevenDayHourPercentageShift(mtgCard.getSevenDayHourPercentageShift());
        mtgCardDTO.setThirtyDayValueShift(mtgCard.getThirtyDayValueShift());
        mtgCardDTO.setThirtyDayPercentageShift(mtgCard.getThirtyDayPercentageShift());
        mtgCardDTO.setAllTimeValueShift(mtgCard.getAllTimeValueShift());
        mtgCardDTO.setAllTimePercentageShift(mtgCard.getAllTimePercentageShift());
        mtgCardDTO.setMtgGoldfishURL(mtgCard.getMtgGoldfishURL());
        mtgCardDTO.setNotes(mtgCard.getNotes());
    }
}
