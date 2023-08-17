package com.neoflex.java2023.service.mapper;

import com.neoflex.java2023.dto.EmploymentDTO;
import com.neoflex.java2023.model.Employment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {
    Employment mapEmploymentJSON(EmploymentDTO employmentDTO);
}
