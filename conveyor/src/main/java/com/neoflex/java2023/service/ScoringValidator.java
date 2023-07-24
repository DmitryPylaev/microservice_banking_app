package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoringValidator {
    public static void validateLoanApplicationRequestDTO (LoanApplicationRequestDTO dto) {
        if (dto.getFirstName().length() > 30 || dto.getFirstName().length() < 2) throw new RuntimeException("Имя неверной длинны");
        if (dto.getLastName().length() > 30 || dto.getLastName().length() < 2) throw new RuntimeException("Фамилия неверной длинны");
        if (dto.getTerm() < 6) throw new RuntimeException("Неверный срок");
        long resultDays = ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now());
        if (resultDays < 18) throw new RuntimeException("Нет 18 лет");
        validateRegexMatch(dto.getEmail(), "[\\w.]{2,50}@[\\w.]{2,20}", new RuntimeException("Не правильный email"));
        validateRegexMatch(dto.getPassportSeries(), "\\d{4}", new RuntimeException("Не правильная серия паспорта"));
        validateRegexMatch(dto.getPassportNumber(), "\\d{6}", new RuntimeException("Не правильный номер паспорта"));
    }

    private static void validateRegexMatch(String str, String regex, RuntimeException runtimeException) {
        if (str == null) throw runtimeException;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) throw runtimeException;
    }
}
