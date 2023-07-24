package com.neoflex.java2023.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ScoringService {

    public BigDecimal evaluateTotalAmountServices(BigDecimal amount, Boolean isInsuranceEnabled) {
        return BigDecimal.valueOf(1.05);
    }

    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        return BigDecimal.valueOf(2.0);
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        return BigDecimal.valueOf(3.0);
    }
}
