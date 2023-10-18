package com.neoflex.java.service.security;

import com.neoflex.java.dto.AccountDTO;
import com.neoflex.java.enums.AccountStatus;
import com.neoflex.java.model.Account;
import com.neoflex.java.repository.AccountRepository;
import com.neoflex.java.service.abstraction.AccountService;
import com.neoflex.java.service.mapper.AccountDTOMapper;
import com.neoflex.java.util.CustomLogger;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private AccountDTOMapper accountDTOMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountDTO getAccountDTO(String username) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(username);
        Account account = accountRepository.findByUsernameAndAccountStatus(username, AccountStatus.ACTIVE).orElse(Account.builder().build());
        CustomLogger.logInfoRequest(account);
        return accountDTOMapper.mapAccountDTO(account);
    }

    @Override
    @PostConstruct
    public void initAdmin() {
        if (accountRepository.findByUsernameAndAccountStatus("root", AccountStatus.ACTIVE).isEmpty()) {
            accountRepository.save(Account.builder()
                    .username("root")
                    .password(passwordEncoder.encode("root"))
                    .accountStatus(AccountStatus.ACTIVE)
                    .build());
        }
    }
}
