package com.neoflex.java2023.service.mapper;

import com.neoflex.java2023.dto.EmploymentDTO;
import com.neoflex.java2023.model.json.EmploymentJSON;

public class EmploymentMapper {
    public static EmploymentJSON mapEmploymentJSON(EmploymentDTO employmentDTO) {
        return EmploymentJSON.builder()
                .employmentStatus(employmentDTO.getEmploymentStatus())
                .employerInn(employmentDTO.getEmployerINN())
                .salary(employmentDTO.getSalary())
                .position(employmentDTO.getPosition())
                .workExperienceTotal(employmentDTO.getWorkExperienceTotal())
                .workExperienceCurrent(employmentDTO.getWorkExperienceCurrent())
                .build();
    }
}
