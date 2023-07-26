package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {OffersService.class, ScoringService.class})
@ComponentScan("com.neoflex.java2023")
class OffersServiceTest {
    @Autowired
    private OffersService offersService;
    @Test
    void getPreparedOffers() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(300000))
                .term(18)
                .firstName("Vasiliy")
                .lastName("Petrov")
                .email("vas@gmail.com")
                .birthdate(LocalDate.parse("1977-08-16"))
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        List<LoanOfferDTO> result = offersService.getPreparedOffers(dto);

        Assertions.assertDoesNotThrow(() ->offersService.getPreparedOffers(dto));
    }
}