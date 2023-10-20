package com.neoflex.java.service.security;

import com.neoflex.java.dto.AccountDTO;
import com.neoflex.java.enums.AccountStatus;
import com.neoflex.java.model.Account;
import com.neoflex.java.repository.AccountRepository;
import com.neoflex.java.service.abstraction.AccountService;
import com.neoflex.java.service.mapper.AccountDTOMapper;
import com.neoflex.java.util.CustomLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountDTOMapper accountDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final String admin;
    private final String defaultPassword;

    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountDTOMapper accountDTOMapper,
                              PasswordEncoder passwordEncoder,
                              @Value("${admin}") String admin,
                              @Value("${password}") String defaultPassword) {
        this.accountRepository = accountRepository;
        this.accountDTOMapper = accountDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.admin = admin;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public AccountDTO getAccountDTO(String username) {
        CustomLogger.logInfoClassAndMethod();
        Account account = accountRepository.findByUsernameAndAccountStatus(username, AccountStatus.ACTIVE).orElse(Account.builder().build());
        return accountDTOMapper.mapAccountDTO(account);
    }

    @Override
    @PostConstruct
    public void initAdmin() {
        if (accountRepository.findByUsernameAndAccountStatus(admin, AccountStatus.ACTIVE).isEmpty()) {
            accountRepository.save(Account.builder()
                    .username(admin)
                    .password(passwordEncoder.encode(defaultPassword))
                    .accountStatus(AccountStatus.ACTIVE)
                    .build());
        }
    }
}
