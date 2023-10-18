package com.neoflex.java.service.mapper;

import com.neoflex.java.dto.AccountDTO;
import com.neoflex.java.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDTOMapper {
    AccountDTO mapAccountDTO(Account account);
}
