package com.neoflex.java.service;

import com.neoflex.java.dto.EmploymentDTO;
import com.neoflex.java.dto.PaymentScheduleElement;
import com.neoflex.java.dto.ScoringDataDTO;
import com.neoflex.java.dto.EmploymentStatus;
import com.neoflex.java.service.abstraction.ScoringService;
import com.neoflex.java.service.exception.CalculateScoringRateException;
import com.neoflex.java.service.properties.ScoringProperties;
import com.neoflex.java.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class ScoringServiceImpl implements ScoringService {
    public static final int MONTH_OF_YEAR = 12;
    public static final int IN_PERCENT_CONVERT_CONSTANT = 100;
    private static final MathContext bigdecimalPrecision = new MathContext(7);

    private final ScoringProperties scoringProperties;

    @Override
    public BigDecimal evaluateTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        CustomLogger.logInfoClassAndMethod();
        return (isInsuranceEnabled) ? amount.subtract(BigDecimal.valueOf(scoringProperties.getInsurancePrice())) : amount;
    }

    @Override
    public BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        CustomLogger.logInfoClassAndMethod();
        BigDecimal rate = BigDecimal.valueOf(scoringProperties.getBaseRate());
        if (isInsuranceEnabled) rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getInsuranceRateDiscount()));
        if (isSalaryClient) rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getSalaryClientRateDiscount()));
        return rate;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        CustomLogger.logInfoClassAndMethod();
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf((long) MONTH_OF_YEAR * IN_PERCENT_CONVERT_CONSTANT), bigdecimalPrecision);
        BigDecimal percent = monthRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthRate.multiply(percent.divide(percent.subtract(BigDecimal.ONE), bigdecimalPrecision));
        return amount.multiply(annuityCoefficient, bigdecimalPrecision);
    }

    @Override
    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term, boolean isInsuranceEnabled) {
        CustomLogger.logInfoClassAndMethod();
        BigDecimal overpayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        overpayment = (isInsuranceEnabled) ? overpayment.add(BigDecimal.valueOf(scoringProperties.getInsurancePrice())) : overpayment;
        BigDecimal termYear = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(MONTH_OF_YEAR), bigdecimalPrecision);
        return overpayment.divide(amount, bigdecimalPrecision)
                .subtract(BigDecimal.ONE)
                .divide(termYear, bigdecimalPrecision)
                .multiply(BigDecimal.valueOf(IN_PERCENT_CONVERT_CONSTANT));
    }

    @Override
    public List<PaymentScheduleElement> paymentScheduleBuild(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment) {
        CustomLogger.logInfoClassAndMethod();
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        for (int i = 0; i < term; i++) {
            BigDecimal monthRate = rate.divide(BigDecimal.valueOf((long) MONTH_OF_YEAR * IN_PERCENT_CONVERT_CONSTANT), bigdecimalPrecision);
            BigDecimal interestPayment = monthRate.multiply(amount, bigdecimalPrecision);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, bigdecimalPrecision);
            amount = amount.subtract(debtPayment);

            paymentSchedule.add(PaymentScheduleElement.builder()
                    .number(i + 1)
                    .date(LocalDate.now().plus(i + 1L, ChronoUnit.MONTHS))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(amount)
                    .build());
        }
        return paymentSchedule;
    }

    @Override
    public BigDecimal calculateScoringRate(ScoringDataDTO scoringDataDTO) {
        CustomLogger.logInfoClassAndMethod();
        BigDecimal rate = BigDecimal.valueOf(scoringProperties.getBaseRate());
        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        try {
            if (employmentDTO.getEmploymentStatus().equals(EmploymentStatus.UNEMPLOYED))
                throw new CalculateScoringRateException("Безработный. Отказ");
            if (scoringDataDTO.getAmount().compareTo(employmentDTO.getSalary().multiply(BigDecimal.valueOf(scoringProperties.getSalaryMinCoefficient()))) > 0)
                throw new CalculateScoringRateException("Низкий доход. Отказ");
            if (employmentDTO.getWorkExperienceTotal() < scoringProperties.getWorkExperienceTotalMin())
                throw new CalculateScoringRateException("Общий стаж недостаточен");
            if (employmentDTO.getWorkExperienceCurrent() < scoringProperties.getWorkExperienceCurrentMin())
                throw new CalculateScoringRateException("Текущий стаж недостаточен");
            long age = ChronoUnit.YEARS.between(scoringDataDTO.getBirthdate(), LocalDate.now());
            if (age < scoringProperties.getMinAge()) throw new CalculateScoringRateException("Нет 18 лет");
            if (age > scoringProperties.getMaxAge()) throw new CalculateScoringRateException("Больше 60 лет");
        } catch (CalculateScoringRateException e) {
            log.info("Отказ в кредите. " + e.getMessage());
            return BigDecimal.valueOf(scoringProperties.getDenyRate());
        }

        log.info("Отказа не произошло");
        if (scoringDataDTO.getDependentAmount() > 1)
            rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getDependentAmountRateDiscount()));
        if (scoringDataDTO.getIsInsuranceEnabled()) rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getInsuranceRateDiscount()));
        if (scoringDataDTO.getIsSalaryClient()) rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getSalaryClientRateDiscount()));

        switch (employmentDTO.getPosition()) {
            case MID_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getMiddleRateDiscount()));
            case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getSeniorRateDiscount()));
        }
        switch (scoringDataDTO.getMaritalStatus()) {
            case SINGLE -> rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getSingleRateDiscount()));
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(scoringProperties.getMarriedRateDiscount()));
        }
        switch (scoringDataDTO.getGender()) {
            case MALE -> log.info("Пол клиента: мужской");
            case FEMALE -> log.info("Пол клиента: женский");
        }

        return rate;
    }
}
