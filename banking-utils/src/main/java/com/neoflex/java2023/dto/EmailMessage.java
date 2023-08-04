package com.neoflex.java2023.dto;

import com.neoflex.java2023.enums.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Электронное письмо")
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
}
