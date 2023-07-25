package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OffersService {

    private final ScoringService scoringService;
    public List<LoanOfferDTO> getPreparedOffers(LoanApplicationRequestDTO dto) {
        return List.of(
                createOffer(false, false, dto),
                createOffer(true, false, dto),
                createOffer(false, true, dto),
                createOffer(true, true, dto)
        );
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient,
                                     LoanApplicationRequestDTO dto) {
        BigDecimal totalAmount = scoringService.evaluateTotalAmountServices(dto.getAmount(), isInsuranceEnabled);
        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        return LoanOfferDTO.builder()
                .requestedAmount(dto.getAmount())
                .totalAmount(totalAmount)
                .term(dto.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .monthlyPayment(scoringService.calculateMonthlyPayment(totalAmount, dto.getTerm(), rate))
                .build();
    }

}
