package com.neoflex.java2023.service.mapper;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.model.Credit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    Credit mapCredit(CreditDTO creditDTO);
}
