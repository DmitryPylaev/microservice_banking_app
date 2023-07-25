package com.neoflex.java2023.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

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
    void evaluateTotalAmountServices() {
        BigDecimal amount = BigDecimal.valueOf(300000);

        BigDecimal totalWitInsurance = service.evaluateTotalAmount(amount, BigDecimal.valueOf(18714.44), 18, false);
        BigDecimal totalWithoutInsurance = service.evaluateTotalAmount(amount, BigDecimal.valueOf(18714.44), 18, true);

        Assertions.assertTrue(BigDecimal.valueOf(637252).doubleValue() - totalWitInsurance.doubleValue() < 500.0);
        Assertions.assertTrue(BigDecimal.valueOf(537252).doubleValue() - totalWithoutInsurance.doubleValue() < 500.0);
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
}