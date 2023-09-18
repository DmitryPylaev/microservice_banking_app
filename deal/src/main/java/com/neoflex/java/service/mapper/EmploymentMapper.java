package com.neoflex.java.service.mapper;

import com.neoflex.java.dto.EmploymentDTO;
import com.neoflex.java.model.Employment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {
    Employment mapEmploymentJSON(EmploymentDTO employmentDTO);
}
