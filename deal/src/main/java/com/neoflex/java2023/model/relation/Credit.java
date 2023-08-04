package com.neoflex.java2023.model.relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.dto.PaymentScheduleElement;
import com.neoflex.java2023.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "credit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private Double monthlyPayment;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "psk")
    private Double psk;

    @Column(name = "payment_schedule")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentScheduleElement> payment_schedule;

    @Column(name = "insurance_enabled")
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
