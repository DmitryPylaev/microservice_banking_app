package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.dto.enums.EmploymentStatus;
import com.neoflex.java2023.dto.enums.MaritalStatus;
import com.neoflex.java2023.dto.enums.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {OffersService.class})
@ComponentScan("com.neoflex.java2023")
class OffersServiceTest {

    @MockBean
    ScoringService scoringService;

    @Autowired
    private OffersService offersService;

    @Test
    void getPreparedOffers() {
        BigDecimal amount = BigDecimal.valueOf(300000);
        BigDecimal amountWithInsurance = amount.subtract(BigDecimal.valueOf(10000));

        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .amount(amount)
                .term(18)
                .firstName("Vasiliy")
                .lastName("Petrov")
                .email("vas@gmail.com")
                .birthdate(LocalDate.parse("1977-08-16"))
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        when(scoringService.calculatePrescoringRate(any(), any())).thenReturn(BigDecimal.valueOf(15).subtract(BigDecimal.valueOf(1.5)),
                BigDecimal.valueOf(15).subtract(BigDecimal.ONE),
                BigDecimal.valueOf(15).subtract(BigDecimal.valueOf(0.5)),
                BigDecimal.valueOf(15));
        when(scoringService.calculateMonthlyPayment(amount, 18, BigDecimal.valueOf(15))).thenReturn(BigDecimal.valueOf(18714.44));
        when(scoringService.evaluateTotalAmount(amount, false)).thenReturn(amount);
        when(scoringService.evaluateTotalAmount(amount, true)).thenReturn(amountWithInsurance);

        List<LoanOfferDTO> result = offersService.getPrescoringOffers(dto);

        assertEquals(4, result.size());
        assertEquals(amount, result.get(0).getRequestedAmount());
        verify(scoringService, Mockito.times(4)).evaluateTotalAmount(any(), any());
        verify(scoringService, Mockito.times(4)).calculatePrescoringRate(any(), any());

        Assertions.assertDoesNotThrow(() -> offersService.getPrescoringOffers(dto));

        assertEquals(amountWithInsurance, result.get(0).getTotalAmount());
        assertEquals(amountWithInsurance, result.get(1).getTotalAmount());
        assertEquals(amount, result.get(2).getTotalAmount());
        assertEquals(amount, result.get(3).getTotalAmount());

    }

    @Test
    void createCreditOffer() {
        BigDecimal amount = BigDecimal.valueOf(20000);
        Integer term = 36;
        BigDecimal monthlyPayment = BigDecimal.valueOf(24970);
        BigDecimal rate = BigDecimal.valueOf(12.0);
        BigDecimal psk = BigDecimal.valueOf(14.978);

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .salary(BigDecimal.valueOf(100000))
                .position(Position.JUNIOR)
                .workExperienceTotal(120)
                .workExperienceCurrent(120)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(amount)
                .term(term)
                .firstName("Vasiliy")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("1977-08-16"))
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(employmentDTO)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        when(scoringService.calculateScoringRate(scoringDataDTO)).thenReturn(rate);
        when(scoringService.calculateMonthlyPayment(amount, term, rate)).thenReturn(monthlyPayment);
        when(scoringService.calculatePsk(amount, monthlyPayment, term, false)).thenReturn(psk);

        CreditDTO creditDTO = offersService.createCreditOffer(scoringDataDTO);

        assertEquals(monthlyPayment, creditDTO.getMonthlyPayment());
        assertEquals(rate, creditDTO.getRate());
        assertEquals(psk, creditDTO.getPsk());

        scoringDataDTO.setBirthdate(LocalDate.parse("2007-08-16"));

        when(scoringService.calculateScoringRate(scoringDataDTO)).thenReturn(BigDecimal.valueOf(999));

        creditDTO = offersService.createCreditOffer(scoringDataDTO);
        assertEquals(BigDecimal.valueOf(999), creditDTO.getRate());
    }
}