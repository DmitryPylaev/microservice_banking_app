package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.service.abstraction.ValidateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ValidateServiceImpl implements ValidateService {
    @Override
    public boolean validatePrescoringRequest(LoanApplicationRequestDTO request) {
        try {
            if (request.getFirstName().length() > 30 || request.getFirstName().length() < 2)
                throw new RuntimeException("Имя неверной длинны");
            if (request.getLastName().length() > 30 || request.getLastName().length() < 2)
                throw new RuntimeException("Фамилия неверной длинны");
            if (request.getTerm() < 6) throw new RuntimeException("Неверный срок");
            long resultDays = ChronoUnit.YEARS.between(request.getBirthdate(), LocalDate.now());
            if (resultDays < 18) throw new RuntimeException("Нет 18 лет");
            validateRegexMatch(request.getEmail(), "[\\w.]{2,50}@[\\w.]{2,20}", new RuntimeException("Не правильный email"));
            validateRegexMatch(request.getPassportSeries(), "\\d{4}", new RuntimeException("Не правильная серия паспорта"));
            validateRegexMatch(request.getPassportNumber(), "\\d{6}", new RuntimeException("Не правильный номер паспорта"));
            log.info("Заявка на прескоринг валидна");
            return true;
        } catch (RuntimeException e) {
            log.info("Заявка на прескоринг не валидна. " + e.getMessage());
            return false;
        }
    }

    private static void validateRegexMatch(String str, String regex, RuntimeException runtimeException) {
        if (str == null) throw runtimeException;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) throw runtimeException;
    }
}