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
public class ScoringServiceImpl implements ScoringService {

    private final Double baseRate;
    private final Integer denyRate;
    private final Integer insurancePrice;
    private final Double salaryClientRateDiscount;
    private final Integer minAge;
    private final Integer maxAge;
    private final Integer salaryMinCoefficient;
    private final Integer workExperienceTotalMin;
    private final Integer workExperienceCurrentMin;
    private final Double marriedRateDiscount;
    private final Double middleRateDiscount;
    private final Double seniorRateDiscount;

    public ScoringServiceImpl(@Value("${baseRate}") Double baseRate,
                              @Value("${denyRate}") Integer denyRate,
                              @Value("${insurancePrice}") Integer insurancePrice,
                              @Value("${salaryClientRateDiscount}") Double salaryClientRateDiscount,
                              @Value("${minAge}") Integer minAge,
                              @Value("${maxAge}") Integer maxAge,
                              @Value("${salaryMinCoefficient}") Integer salaryMinCoefficient,
                              @Value("${workExperienceTotalMin}") Integer workExperienceTotalMin,
                              @Value("${workExperienceCurrentMin}") Integer workExperienceCurrentMin,
                              @Value("${marriedRateDiscount}") Double marriedRateDiscount,
                              @Value("${middleRateDiscount}") Double middleRateDiscount,
                              @Value("${seniorRateDiscount}") Double seniorRateDiscount) {
        this.baseRate = baseRate;
        this.denyRate = denyRate;
        this.insurancePrice = insurancePrice;
        this.salaryClientRateDiscount = salaryClientRateDiscount;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.salaryMinCoefficient = salaryMinCoefficient;
        this.workExperienceTotalMin = workExperienceTotalMin;
        this.workExperienceCurrentMin = workExperienceCurrentMin;
        this.marriedRateDiscount = marriedRateDiscount;
        this.middleRateDiscount = middleRateDiscount;
        this.seniorRateDiscount = seniorRateDiscount;
    }

    @Override
    public BigDecimal evaluateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        return (isInsuranceEnabled) ? amount.subtract(BigDecimal.valueOf(insurancePrice)) : amount;
    }

    @Override
    public BigDecimal calculatePrescoringRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        if (isInsuranceEnabled) rate = rate.subtract(BigDecimal.ONE);
        if (isSalaryClient) rate = rate.subtract(BigDecimal.valueOf(salaryClientRateDiscount));
        return rate;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(7));
        BigDecimal percent = monthRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthRate.multiply(percent.divide(percent.subtract(BigDecimal.ONE), new MathContext(7)));
        return amount.multiply(annuityCoefficient, new MathContext(7));
    }

    @Override
    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term, Boolean isInsuranceEnabled) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        BigDecimal overpayment = monthlyPayment.multiply(BigDecimal.valueOf(term));
        overpayment = (isInsuranceEnabled) ? overpayment.add(BigDecimal.valueOf(insurancePrice)) : overpayment;
        BigDecimal termYear = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), new MathContext(7));
        return overpayment.divide(amount, new MathContext(7))
                .subtract(BigDecimal.ONE)
                .divide(termYear, new MathContext(7))
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public List<PaymentScheduleElement> paymentScheduleBuild(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        for (int i = 0; i < term; i++) {
            BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12 * 100), new MathContext(6));
            BigDecimal interestPayment = monthRate.multiply(amount, new MathContext(6));
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

    @Override
    public BigDecimal calculateScoringRate(ScoringDataDTO scoringDataDTO) {
        log.info("В методе сервиса скоринга: " + new Exception().getStackTrace()[1].getMethodName());
        BigDecimal rate = BigDecimal.valueOf(baseRate);
        EmploymentDTO employmentDTO = scoringDataDTO.getEmployment();
        try {
            if (employmentDTO.getEmploymentStatus().equals(EmploymentStatus.UNEMPLOYED))
                throw new RuntimeException("Безработный. Отказ");
            if (scoringDataDTO.getAmount().compareTo(employmentDTO.getSalary().multiply(BigDecimal.valueOf(salaryMinCoefficient))) > 0)
                throw new RuntimeException("Низкий доход. Отказ");
            if (employmentDTO.getWorkExperienceTotal() < workExperienceTotalMin)
                throw new RuntimeException("Общий стаж недостаточен");
            if (employmentDTO.getWorkExperienceCurrent() < workExperienceCurrentMin)
                throw new RuntimeException("Текущий стаж недостаточен");
            long age = ChronoUnit.YEARS.between(scoringDataDTO.getBirthdate(), LocalDate.now());
            if (age < minAge) throw new RuntimeException("Нет 18 лет");
            if (age > maxAge) throw new RuntimeException("Больше 60 лет");
        } catch (RuntimeException e) {
            log.info("Отказ в кредите. " + e.getMessage());
            return BigDecimal.valueOf(denyRate);
        }

        log.info("Отказа не произошло");
        if (scoringDataDTO.getDependentAmount() > 1) rate = rate.add(BigDecimal.ONE);
        if (scoringDataDTO.getIsInsuranceEnabled()) rate = rate.subtract(BigDecimal.ONE);
        if (scoringDataDTO.getIsSalaryClient()) rate = rate.subtract(BigDecimal.valueOf(salaryClientRateDiscount));

        switch (employmentDTO.getPosition()) {
            case MIDDLE -> rate = rate.subtract(BigDecimal.valueOf(middleRateDiscount));
            case SENIOR -> rate = rate.subtract(BigDecimal.valueOf(seniorRateDiscount));
        }
        switch (scoringDataDTO.getMaritalStatus()) {
            case SINGLE -> rate = rate.add(BigDecimal.ONE);
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(marriedRateDiscount));
        }
        switch (scoringDataDTO.getGender()) {
            case MALE -> log.info("Пол клиента: мужской");
            case FEMALE -> log.info("Пол клиента: женский");
        }

        return rate;
    }
}
