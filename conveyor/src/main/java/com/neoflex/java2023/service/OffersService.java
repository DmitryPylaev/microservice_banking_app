package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Log4j2
public class OffersService {

    private final ScoringService scoringService;

    public List<LoanOfferDTO> getPrescoringOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("В методе сервиса подготовки предложений: " + new Exception().getStackTrace()[1].getMethodName());
        return Stream.of(
                createPrescoringOffer(true, true, loanApplicationRequestDTO),
                createPrescoringOffer(true, false, loanApplicationRequestDTO),
                createPrescoringOffer(false, true, loanApplicationRequestDTO),
                createPrescoringOffer(false, false, loanApplicationRequestDTO)
        ).sorted(Comparator.comparing(LoanOfferDTO::getRate)).toList();
    }

    private LoanOfferDTO createPrescoringOffer(Boolean isInsuranceEnabled,
                                               Boolean isSalaryClient,
                                               LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("В методе сервиса подготовки предложений OffersService::createPrescoringOffer");
        BigDecimal amount = loanApplicationRequestDTO.getAmount();
        Integer term = loanApplicationRequestDTO.getTerm();

        BigDecimal rate = scoringService.calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = scoringService.evaluateTotalAmount(amount, isInsuranceEnabled);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(amount, term, rate);

        return LoanOfferDTO.builder()
                .requestedAmount(amount)
                .totalAmount(totalAmount)
                .term(term)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .rate(rate)
                .monthlyPayment(monthlyPayment)
                .build();
    }

    public CreditDTO createCreditOffer(ScoringDataDTO scoringDataDTO) {
        log.info("В методе сервиса подготовки предложений: " + new Exception().getStackTrace()[1].getMethodName());
        BigDecimal amount = scoringDataDTO.getAmount();
        Integer term = scoringDataDTO.getTerm();
        Boolean isInsuranceEnabled = scoringDataDTO.getIsInsuranceEnabled();
        Boolean isSalaryClient = scoringDataDTO.getIsSalaryClient();

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
                .isSalaryClient(isSalaryClient)
                .paymentSchedule(paymentSchedule)
                .build();
    }

}
