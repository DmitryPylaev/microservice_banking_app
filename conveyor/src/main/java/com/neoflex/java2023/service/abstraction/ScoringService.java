package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.PaymentScheduleElement;
import com.neoflex.java2023.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ScoringService {
    BigDecimal evaluateTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);

    BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient);

    BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate);

    BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term, boolean isInsuranceEnabled);

    List<PaymentScheduleElement> paymentScheduleBuild(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment);

    BigDecimal calculateScoringRate(ScoringDataDTO scoringDataDTO);
}
