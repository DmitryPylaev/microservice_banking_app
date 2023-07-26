package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OffersService {

    private final ScoringService scoringService;
    public List<LoanOfferDTO> getPreparedOffers(LoanApplicationRequestDTO dto) {
        return Stream.of(
                createOffer(false, false, dto),
                createOffer(true, false, dto),
                createOffer(false, true, dto),
                createOffer(true, true, dto)
        ).sorted(Comparator.comparing(LoanOfferDTO::getRate)).toList();
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient,
                                     LoanApplicationRequestDTO dto) {

        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = scoringService.evaluateTotalAmount(dto.getAmount(), isInsuranceEnabled);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(dto.getAmount(), dto.getTerm(), rate);

        return LoanOfferDTO.builder()
                .requestedAmount(dto.getAmount())
                .totalAmount(totalAmount)
                .term(dto.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .monthlyPayment(monthlyPayment)
                .build();
    }

}
