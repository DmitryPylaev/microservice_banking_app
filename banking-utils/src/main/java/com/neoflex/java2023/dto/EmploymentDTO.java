package com.neoflex.java2023.dto;

import com.neoflex.java2023.enums.EmploymentPosition;
import com.neoflex.java2023.enums.EmploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о занятости клиента")
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
