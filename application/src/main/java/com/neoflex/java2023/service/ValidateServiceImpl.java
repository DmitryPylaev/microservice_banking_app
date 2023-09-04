package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.service.abstraction.ValidateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ValidateServiceImpl implements ValidateService {
    private final Integer nameLengthMin;
    private final Integer nameLengthMax;
    private final Integer minTerm;
    private final Integer minAge;
    private final String emailPattern;
    private final String passportSeriesPattern;
    private final String passportNumberPattern;

    public ValidateServiceImpl(@Value("${nameLengthMin}") Integer nameLengthMin,
                               @Value("${nameLengthMax}") Integer nameLengthMax,
                               @Value("${minTerm}") Integer minTerm,
                               @Value("${minAge}") Integer minAge,
                               @Value("${emailPattern.regexp}") String emailPattern,
                               @Value("${passportSeriesPattern.regexp}") String passportSeriesPattern,
                               @Value("${passportNumberPattern.regexp}") String passportNumberPattern) {
        this.nameLengthMin = nameLengthMin;
        this.nameLengthMax = nameLengthMax;
        this.minTerm = minTerm;
        this.minAge = minAge;
        this.emailPattern = emailPattern;
        this.passportSeriesPattern = passportSeriesPattern;
        this.passportNumberPattern = passportNumberPattern;
    }

    @Override
    public boolean validatePrescoringRequest(LoanApplicationRequestDTO request) {
        try {
            if (request.getFirstName().length() > nameLengthMax || request.getFirstName().length() < nameLengthMin)
                throw new RuntimeException("Имя не правильной длины");
            if (request.getLastName().length() > nameLengthMax || request.getLastName().length() < nameLengthMin)
                throw new RuntimeException("Фамилия не правильной длины");
            if (request.getTerm() < minTerm) throw new RuntimeException("Неправильный срок");
            long resultYears = ChronoUnit.YEARS.between(request.getBirthdate(), LocalDate.now());
            if (resultYears < minAge) throw new RuntimeException("Нет 18 лет");
            validateRegexMatch(request.getEmail(), emailPattern, new RuntimeException("Неправильный email"));
            validateRegexMatch(request.getPassportSeries(), passportSeriesPattern, new RuntimeException("Неправильная серия паспорта"));
            validateRegexMatch(request.getPassportNumber(), passportNumberPattern, new RuntimeException("Неправильный номер паспорта"));
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