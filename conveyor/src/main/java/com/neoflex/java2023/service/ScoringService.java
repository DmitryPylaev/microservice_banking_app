package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.EmploymentDTO;
import com.neoflex.java2023.dto.PaymentScheduleElement;
import com.neoflex.java2023.dto.ScoringDataDTO;
import com.neoflex.java2023.dto.enums.EmploymentStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ScoringService {

    private final Double baseRate;

    public ScoringService(@Value("${baseRate}") Double baseRate) {
        this.baseRate = baseRate;
    }

    public BigDecimal evaluateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        return (isInsuranceEnabled) ? amount.subtract(BigDecimal.valueOf(10000)) : amount;
    }

    public BigDecimal calculatePrescoringRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        if (isInsuranceEnabled) rate = rate.subtract(BigDecimal.ONE);
        if (isSalaryClient) rate = rate.subtract(BigDecimal.valueOf(0.5));
        return rate;
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(7));
        BigDecimal percent = monthRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthRate.multiply(percent.divide(percent.subtract(BigDecimal.ONE), new MathContext(7)));
        return amount.multiply(annuityCoefficient, new MathContext(7));
    }

    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term, Boolean isInsuranceEnabled) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        BigDecimal overpayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        overpayment = (isInsuranceEnabled) ? overpayment.add(BigDecimal.valueOf(100000)) : overpayment;
        BigDecimal termYear = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), new MathContext(7));
        return overpayment.divide(amount, new MathContext(7))
                .subtract(BigDecimal.ONE)
                .divide(termYear, new MathContext(7))
                .multiply(BigDecimal.valueOf(100));
    }

    public List<PaymentScheduleElement> paymentScheduleBuild(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        for (int i = 0; i < term; i++) {
            BigDecimal interestPayment = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(6)).multiply(amount, new MathContext(6));
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, new MathContext(6));
            amount = amount.subtract(debtPayment);

            paymentSchedule.add(PaymentScheduleElement.builder()
                    .number(i + 1)
                    .date(LocalDate.now().plus(i + 1, ChronoUnit.MONTHS))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(amount)
                    .build());
        }
        return paymentSchedule;
    }

    public BigDecimal calculateScoringRate(ScoringDataDTO scoringDataDTO) {
        log.info("В методе сервиса скоринга: " +  new Exception().getStackTrace()[1].getMethodName());
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        try {
            if (employmentDTO.getEmploymentStatus().equals(EmploymentStatus.UNEMPLOYED))
                throw new RuntimeException("Безработный. Отказ");
            if (scoringDataDTO.getAmount().compareTo(employmentDTO.getSalary().multiply(BigDecimal.valueOf(20))) > 0)
                throw new RuntimeException("Низкий доход. Отказ");
            if (employmentDTO.getWorkExperienceTotal() < 12) throw new RuntimeException("Общий стаж недостаточен");
            if (employmentDTO.getWorkExperienceCurrent() < 3) throw new RuntimeException("Текущий стаж недостаточен");
            long age = ChronoUnit.YEARS.between(scoringDataDTO.getBirthdate(), LocalDate.now());
            if (age < 18) throw new RuntimeException("Нет 18 лет");
            if (age > 60) throw new RuntimeException("Больше 60 лет");
        } catch (RuntimeException e) {
            log.info("Отказ в кредите. " + e.getMessage());
            return BigDecimal.valueOf(999);
        }

        log.info("Отказа не произошло");
        if (scoringDataDTO.getDependentAmount() > 1) rate = rate.add(BigDecimal.ONE);
        if (scoringDataDTO.getIsInsuranceEnabled()) rate = rate.subtract(BigDecimal.ONE);
        if (scoringDataDTO.getIsSalaryClient()) rate = rate.subtract(BigDecimal.valueOf(0.5));

        switch (employmentDTO.getPosition()) {
            case MIDDLE -> rate = rate.subtract(BigDecimal.valueOf(2));
            case SENIOR -> rate = rate.subtract(BigDecimal.valueOf(4));
        }
        switch (scoringDataDTO.getMaritalStatus()) {
            case SINGLE -> rate = rate.add(BigDecimal.ONE);
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3));
        }

        return rate;
    }
}
