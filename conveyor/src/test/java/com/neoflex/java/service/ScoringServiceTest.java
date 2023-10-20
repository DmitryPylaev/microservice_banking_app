package com.neoflex.java.service;

import com.neoflex.java.app.ConveyorApplication;
import com.neoflex.java.dto.EmploymentDTO;
import com.neoflex.java.dto.PaymentScheduleElement;
import com.neoflex.java.dto.ScoringDataDTO;
import com.neoflex.java.enums.EmploymentPosition;
import com.neoflex.java.enums.EmploymentStatus;
import com.neoflex.java.enums.Gender;
import com.neoflex.java.enums.MaritalStatus;
import com.neoflex.java.service.abstraction.ScoringService;
import com.neoflex.java.service.properties.ScoringProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ConveyorApplication.class})
@ComponentScan("com.neoflex.java")
@ExtendWith(OutputCaptureExtension.class)
class ScoringServiceTest {
    @Autowired
    private ScoringProperties scoringProperties;

    @Autowired
    private ScoringService service;

    private final EmploymentDTO employmentDTO = EmploymentDTO.builder()
            .employmentStatus(EmploymentStatus.EMPLOYED)
            .salary(BigDecimal.valueOf(100000))
            .position(EmploymentPosition.MID_MANAGER)
            .workExperienceTotal(120)
            .workExperienceCurrent(120)
            .build();

    private final ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
            .amount(BigDecimal.valueOf(20000))
            .term(36)
            .firstName("Vasiliy")
            .lastName("Petrov")
            .middleName("Mid")
            .birthdate(LocalDate.parse("1977-08-16"))
            .maritalStatus(MaritalStatus.MARRIED)
            .gender(Gender.MALE)
            .dependentAmount(1)
            .employment(employmentDTO)
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();

    @Test
    void evaluateTotalAmount() {
        BigDecimal amount = BigDecimal.valueOf(100000);

        assertEquals(amount, service.evaluateTotalAmount(amount, false));
        assertEquals(amount.subtract(BigDecimal.valueOf(scoringProperties.getInsurancePrice()), new MathContext(7)), service.evaluateTotalAmount(amount, true));
    }

    @Test
    void calculateRate() {
        assertEquals(BigDecimal.valueOf(scoringProperties.getBaseRate()).subtract(BigDecimal.valueOf(scoringProperties.getInsuranceRateDiscount() + scoringProperties.getSalaryClientRateDiscount())),
                service.calculatePrescoringRate(true, true));

        assertEquals(BigDecimal.valueOf(scoringProperties.getBaseRate()).subtract(BigDecimal.valueOf(scoringProperties.getInsuranceRateDiscount())),
                service.calculatePrescoringRate(true, false));

        assertEquals(BigDecimal.valueOf(scoringProperties.getBaseRate()).subtract(BigDecimal.valueOf(scoringProperties.getSalaryClientRateDiscount())),
                service.calculatePrescoringRate(false, true));

        assertEquals(BigDecimal.valueOf(scoringProperties.getBaseRate()),
                service.calculatePrescoringRate(false, false));
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal expected = BigDecimal.valueOf(18714.44);
        BigDecimal result = service.calculateMonthlyPayment(BigDecimal.valueOf(300000), 18, BigDecimal.valueOf(15));
        assertTrue(Math.abs(expected.doubleValue() - result.doubleValue()) < 2.0);
    }

    @Test
    void calcPsk() {
        BigDecimal amount = BigDecimal.valueOf(1000000);

        BigDecimal totalWitInsurance = service.calculatePsk(amount, BigDecimal.valueOf(47125), 24, false);
        BigDecimal totalWithoutInsurance = service.calculatePsk(amount, BigDecimal.valueOf(47125), 24, true);

        assertTrue(BigDecimal.valueOf(6.55).doubleValue() - totalWitInsurance.doubleValue() < 100.0);
        assertTrue(BigDecimal.valueOf(11.55).doubleValue() - totalWithoutInsurance.doubleValue() < 100.0);
    }

    @Test
    void paymentScheduleBuild() {
        BigDecimal amount = BigDecimal.valueOf(20002);
        BigDecimal monthlyPayment = BigDecimal.valueOf(664.29);

        List<PaymentScheduleElement> paymentSchedule = service.paymentScheduleBuild(amount, 36, BigDecimal.valueOf(12), monthlyPayment);

        PaymentScheduleElement expected = PaymentScheduleElement.builder()
                .number(11)
                .date(LocalDate.now().plus(11, ChronoUnit.MONTHS))
                .totalPayment(monthlyPayment)
                .interestPayment(BigDecimal.valueOf(151.4471))
                .debtPayment(BigDecimal.valueOf(512.8429))
                .remainingDebt(BigDecimal.valueOf(14631.8657))
                .build();

        assertEquals(expected, paymentSchedule.get(10));
    }

    @Test
    void calculateScoringRateOK(CapturedOutput output) {
        assertEquals(BigDecimal.valueOf(10.0), service.calculateScoringRate(scoringDataDTO));

        employmentDTO.setPosition(EmploymentPosition.TOP_MANAGER);

        assertEquals(BigDecimal.valueOf(8.0), service.calculateScoringRate(scoringDataDTO));

        scoringDataDTO.setMaritalStatus(MaritalStatus.SINGLE);

        assertEquals(BigDecimal.valueOf(12.0), service.calculateScoringRate(scoringDataDTO));
        assertTrue(output.getOut().contains("Пол клиента: мужской"));

        scoringDataDTO.setGender(Gender.FEMALE);

        assertEquals(BigDecimal.valueOf(12.0), service.calculateScoringRate(scoringDataDTO));
        assertTrue(output.getOut().contains("Пол клиента: женский"));
    }

    @Test
    void calculateScoringRateReject(CapturedOutput output) {
        scoringDataDTO.setBirthdate(LocalDate.parse("2007-08-16"));

        BigDecimal expectedRate = BigDecimal.valueOf(scoringProperties.getDenyRate());

        assertEquals(expectedRate, service.calculateScoringRate(scoringDataDTO));
        assertTrue(output.getOut().contains("Отказ в кредите. Нет 18 лет"));

        scoringDataDTO.setBirthdate(LocalDate.parse("1997-08-16"));
        employmentDTO.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        assertEquals(expectedRate, service.calculateScoringRate(scoringDataDTO));
        assertTrue(output.getOut().contains("Отказ в кредите. Безработный"));

        employmentDTO.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDTO.setSalary(BigDecimal.valueOf(10));

        assertEquals(expectedRate, service.calculateScoringRate(scoringDataDTO));
        assertTrue(output.getOut().contains("Отказ в кредите. Низкий доход"));
    }

}