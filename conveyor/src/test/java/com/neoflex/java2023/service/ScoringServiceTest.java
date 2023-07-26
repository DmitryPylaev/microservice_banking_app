package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.PaymentScheduleElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest(classes = {ScoringService.class})
@ComponentScan("com.neoflex.java2023")
class ScoringServiceTest {
    private final Double baseRate;
    private final ScoringService service;

    @Autowired
    public ScoringServiceTest(@Value("${baseRate}") Double baseRate, ScoringService service) {
        this.baseRate = baseRate;
        this.service = service;
    }

    @Test
    void evaluateTotalAmount() {
        BigDecimal amount = BigDecimal.valueOf(100);

        Assertions.assertEquals(amount, service.evaluateTotalAmount(amount, false));
        Assertions.assertEquals(amount.subtract(BigDecimal.valueOf(100), new MathContext(7)), service.evaluateTotalAmount(amount, true));
    }

    @Test
    void calculateRate() {
        Assertions.assertEquals(BigDecimal.valueOf(baseRate).subtract(BigDecimal.valueOf(1.5)),
                service.calculateRate(true, true));

        Assertions.assertEquals(BigDecimal.valueOf(baseRate).subtract(BigDecimal.valueOf(1)),
                service.calculateRate(true, false));

        Assertions.assertEquals(BigDecimal.valueOf(baseRate).subtract(BigDecimal.valueOf(0.5)),
                service.calculateRate(false, true));

        Assertions.assertEquals(BigDecimal.valueOf(baseRate),
                service.calculateRate(false, false));
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal expected = BigDecimal.valueOf(18714.44);
        BigDecimal result = service.calculateMonthlyPayment(BigDecimal.valueOf(300000), 18, BigDecimal.valueOf(15));
        Assertions.assertTrue(Math.abs(expected.doubleValue() - result.doubleValue()) < 2.0);
    }

    @Test
    void calcPsk() {
        BigDecimal amount = BigDecimal.valueOf(1000000);

        BigDecimal totalWitInsurance = service.calcPsk(amount, BigDecimal.valueOf(47125), 24, false);
        BigDecimal totalWithoutInsurance = service.calcPsk(amount, BigDecimal.valueOf(47125), 24, true);

        Assertions.assertTrue(BigDecimal.valueOf(6.55).doubleValue() - totalWitInsurance.doubleValue() < 100.0);
        Assertions.assertTrue(BigDecimal.valueOf(11.55).doubleValue() - totalWithoutInsurance.doubleValue() < 100.0);
    }

    @Test
    void paymentScheduleBuild() {
        BigDecimal amount = BigDecimal.valueOf(20000);
        BigDecimal monthlyPayment = BigDecimal.valueOf(664.29);

        List<PaymentScheduleElement> paymentSchedule = service.paymentScheduleBuild(amount, 36, BigDecimal.valueOf(12), monthlyPayment);

        PaymentScheduleElement expected = PaymentScheduleElement.builder()
                .number(11)
                .date(LocalDate.now().plus(11, ChronoUnit.MONTHS))
                .totalPayment(monthlyPayment)
                .interestPayment(BigDecimal.valueOf(151.425))
                .debtPayment(BigDecimal.valueOf(512.865))
                .remainingDebt(BigDecimal.valueOf(14629.635))
                .build();

        Assertions.assertEquals(expected, paymentSchedule.get(10));
    }
}