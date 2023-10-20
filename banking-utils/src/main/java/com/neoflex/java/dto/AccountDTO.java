package com.neoflex.java.dto;

import com.neoflex.java.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Аккаунт")
public class AccountDTO {
    private long id;
    private String username;
    private String password;
    private AccountStatus accountStatus;
}
