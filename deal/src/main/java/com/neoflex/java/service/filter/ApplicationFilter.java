package com.neoflex.java.service.filter;

import com.neoflex.java.dto.ApplicationStatus;
import com.neoflex.java.dto.Gender;
import com.neoflex.java.dto.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ApplicationFilter {
    private ApplicationStatus status;
    private MaritalStatus maritalStatus;
    private Gender gender;
    private BigDecimal amount;
    private Integer term;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
