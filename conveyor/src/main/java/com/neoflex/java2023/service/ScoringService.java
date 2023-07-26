package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.PaymentScheduleElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScoringService {

    private final Double baseRate;

    public ScoringService(@Value("${baseRate}") Double baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal evaluateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        return (isInsuranceEnabled)?amount.subtract(BigDecimal.valueOf(100)):amount;
    }

    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        BigDecimal result = BigDecimal.valueOf(baseRate);
        if (isInsuranceEnabled) result = result.subtract(BigDecimal.ONE);
        if (isSalaryClient) result = result.subtract(BigDecimal.valueOf(0.5));
        return result;
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(7));
        BigDecimal percent = monthRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthRate.multiply(percent.divide(percent.subtract(BigDecimal.ONE), new MathContext(7)));
        return amount.multiply(annuityCoefficient, new MathContext(7));
    }

    public BigDecimal calcPsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term, Boolean isInsuranceEnabled) {
        BigDecimal overpayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        overpayment = (isInsuranceEnabled)?overpayment.add(BigDecimal.valueOf(100000)):overpayment;
        BigDecimal termYear = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), new MathContext(7));
        return overpayment.divide(amount, new MathContext(7))
                .subtract(BigDecimal.ONE)
                .divide(termYear, new MathContext(7))
                .multiply(BigDecimal.valueOf(100));
    }

    public List<PaymentScheduleElement> paymentScheduleBuild(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment) {
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        for (int i = 0; i < term; i++) {
            BigDecimal interestPayment = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(6)).multiply(amount, new MathContext(6));
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, new MathContext(6));
            amount = amount.subtract(debtPayment);

            paymentSchedule.add(PaymentScheduleElement.builder()
                    .number(i+1)
                    .date(LocalDate.now().plus(i + 1, ChronoUnit.MONTHS))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(amount)
                    .build());
        }
        return paymentSchedule;
    }
}
