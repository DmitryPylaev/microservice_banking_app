package com.neoflex.java.service;

import com.neoflex.java.dto.AccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private FeignDeal feignDeal;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDTO account = feignDeal.getAccountByUsername(username);
        if (account.getUsername().isEmpty()) throw new UsernameNotFoundException("User not found");
        return new User(username, account.getPassword(), Collections.emptyList());
    }
}
