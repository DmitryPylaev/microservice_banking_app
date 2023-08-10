package com.neoflex.java2023.service.mapper;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.enums.CreditStatus;
import com.neoflex.java2023.model.relation.Credit;
import com.neoflex.java2023.util.CustomLogger;

public class CreditMapper {
    public static Credit mapCredit(CreditDTO creditDTO) {
        CustomLogger.logInfoClassAndMethod();
        return Credit.builder()
                .amount(creditDTO.getAmount())
                .term(creditDTO.getTerm())
                .monthlyPayment(creditDTO.getMonthlyPayment())
                .rate(creditDTO.getRate())
                .psk(creditDTO.getPsk())
                .paymentSchedule(creditDTO.getPaymentSchedule())
                .insuranceEnabled(creditDTO.getIsInsuranceEnabled())
                .salaryClient(creditDTO.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();
    }
}
