package com.neoflex.java2023.dto;

import com.neoflex.java2023.dto.enums.EmploymentStatus;
import com.neoflex.java2023.dto.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Schema(description = "")
public class EmploymentDTO {
  private EmploymentStatus employmentStatus;
  private String employerINN;
  private BigDecimal salary;
  private Position position;
  private Integer workExperienceTotal;
  private Integer workExperienceCurrent;
}
