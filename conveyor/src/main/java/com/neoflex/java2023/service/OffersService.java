package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
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
    public List<LoanOfferDTO> getPrescoringOffers(LoanApplicationRequestDTO dto) {
        return Stream.of(
                createPrescoringOffer(true, true, dto),
                createPrescoringOffer(true, false, dto),
                createPrescoringOffer(false, true, dto),
                createPrescoringOffer(false, false, dto)
        ).sorted(Comparator.comparing(LoanOfferDTO::getRate)).toList();
    }

    private LoanOfferDTO createPrescoringOffer(Boolean isInsuranceEnabled,
                                               Boolean isSalaryClient,
                                               LoanApplicationRequestDTO dto) {

        BigDecimal rate = scoringService.calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
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

    public CreditDTO createCreditOffer(ScoringDataDTO scoringDataDTO) {
        BigDecimal amount = scoringDataDTO.getAmount();
        Integer term = scoringDataDTO.getTerm();
        Boolean isInsuranceEnabled = scoringDataDTO.getIsInsuranceEnabled();

        BigDecimal rate = scoringService.calculateScoringRate(scoringDataDTO);
        BigDecimal totalAmount = scoringService.evaluateTotalAmount(amount, isInsuranceEnabled);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(amount, term, rate);
        BigDecimal psk = scoringService.calculatePsk(amount, monthlyPayment, term, isInsuranceEnabled);
        List<PaymentScheduleElement> paymentSchedule = scoringService.paymentScheduleBuild(amount, term, rate, monthlyPayment);

        return CreditDTO.builder()
                .amount(totalAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }

}
