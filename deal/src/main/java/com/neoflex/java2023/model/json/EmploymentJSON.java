package com.neoflex.java2023.model.json;

import com.neoflex.java2023.enums.EmploymentPosition;
import com.neoflex.java2023.enums.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentJSON implements Serializable {
    private EmploymentStatus status;
    private String employerInn;
    private Double salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
