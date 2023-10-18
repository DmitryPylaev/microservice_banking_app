package com.neoflex.java.repository;

import com.neoflex.java.model.Account;
import com.neoflex.java.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsernameAndAccountStatus(String username, AccountStatus accountStatus);
}
