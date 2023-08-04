package com.neoflex.java2023.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoflex.java2023.util.CustomBigDecimalSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Предложение по кредиту после прескоринга")
public class LoanOfferDTO implements Serializable {
    private Long applicationId;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal requestedAmount;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal totalAmount;

    private Integer term;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal monthlyPayment;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal rate;

    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
