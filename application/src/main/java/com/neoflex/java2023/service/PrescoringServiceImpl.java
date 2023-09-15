package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.service.abstraction.PrescoringService;
import com.neoflex.java2023.service.abstraction.ValidateService;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class PrescoringServiceImpl implements PrescoringService {

    private FeignDeal feignDeal;
    private ValidateService validateService;

    @Override
    public List<LoanOfferDTO> getPreparedOffers(LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        if (validateService.validatePrescoringRequest(request)) return feignDeal.getOffers(request);
        else return new ArrayList<>();
    }

    @Override
    public void executeSpecifyApplicationRequest(LoanOfferDTO request) {
        CustomLogger.logInfoClassAndMethod();
        try {
            feignDeal.specifyApplication(request);
            log.info("Заявка обработана");
        } catch (Exception e) {
            log.info("Ошибка сети " + e.getMessage());
        }
    }
}