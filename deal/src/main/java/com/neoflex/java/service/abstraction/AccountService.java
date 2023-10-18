package com.neoflex.java.service.abstraction;

import com.neoflex.java.dto.AccountDTO;
import jakarta.annotation.PostConstruct;

public interface AccountService {
    AccountDTO getAccountDTO(String username);

    @PostConstruct
    void initAdmin();
}
