package com.neoflex.java2023.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.neoflex.java2023.util.CustomBigDecimalSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "График платежей")
public class PaymentScheduleElement {
    private Integer number;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal totalPayment;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal interestPayment;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal debtPayment;

    @JsonSerialize(using = CustomBigDecimalSerializer.class)
    private BigDecimal remainingDebt;
}
