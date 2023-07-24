package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ScoringValidatorTest {

    @Test
    void validateIsOk() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .firstName("Vasiliy")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("1977-08-16"))
                .term(12)
                .email("vas@gmail.com")
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        ScoringValidator.validateLoanApplicationRequestDTO(dto);
    }

    @Test
    void validateFirstNameFail() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .firstName("V")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("1977-08-16"))
                .term(12)
                .email("vas@gmail.com")
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        Exception exception = Assertions.assertThrows(Exception.class, () -> ScoringValidator.validateLoanApplicationRequestDTO(dto));
        Assertions.assertEquals("Имя неверной длинны", exception.getMessage());
    }

    @Test
    void validateBirthday() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .firstName("Vasiliy")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("2007-08-16"))
                .term(12)
                .email("vas@gmail.com")
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        Exception exception = Assertions.assertThrows(Exception.class, () -> ScoringValidator.validateLoanApplicationRequestDTO(dto));
        Assertions.assertEquals("Нет 18 лет", exception.getMessage());
    }

    @Test
    void validateEmail() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .firstName("Vasiliy")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("1997-08-16"))
                .term(12)
                .email("v@gmail.com")
                .passportSeries("5766")
                .passportNumber("576687")
                .build();

        Exception exception = Assertions.assertThrows(Exception.class, () -> ScoringValidator.validateLoanApplicationRequestDTO(dto));
        Assertions.assertEquals("Не правильный email", exception.getMessage());
    }

    @Test
    void validatePassportSeries() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .firstName("Vasiliy")
                .lastName("Petrov")
                .birthdate(LocalDate.parse("1997-08-16"))
                .term(12)
                .email("vas@gmail.com")
                .passportSeries("5767676")
                .passportNumber("576687")
                .build();

        Exception exception = Assertions.assertThrows(Exception.class, () -> ScoringValidator.validateLoanApplicationRequestDTO(dto));
        Assertions.assertEquals("Не правильная серия паспорта", exception.getMessage());
    }
}