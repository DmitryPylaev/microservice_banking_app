package com.neoflex.java2023.model.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoflex.java2023.enums.EmploymentPosition;
import com.neoflex.java2023.enums.EmploymentStatus;
import com.neoflex.java2023.util.CustomBigDecimalSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentJSON implements Serializable {
    private EmploymentStatus employmentStatus;
    private String employerInn;
    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
