package com.neoflex.java2023.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class ScoringService {

    private final Double baseRate;

    public ScoringService(@Value("${baseRate}") Double baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal evaluateTotalAmount(BigDecimal amount, BigDecimal monthlyPayment, Integer term, Boolean isInsuranceEnabled) {
        BigDecimal overpayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        amount = amount.add(overpayment);
        return (isInsuranceEnabled)?amount.add(BigDecimal.valueOf(100000)):amount;
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
}
