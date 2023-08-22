package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.service.abstraction.PrescoringService;
import com.neoflex.java2023.service.abstraction.ValidateService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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
        if (validateService.validatePrescoringRequest(request)) return feignDeal.getOffers(request);
        else return new ArrayList<>();
    }

    @Override
    public ResponseEntity<String> executeSpecifyApplicationRequest(LoanOfferDTO request) {
        try {
            feignDeal.specifyApplication(request);
            return ResponseEntity.ok("Заявка обработана");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка сети " + e.getMessage());
        }
    }
}