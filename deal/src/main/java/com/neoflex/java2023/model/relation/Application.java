package com.neoflex.java2023.model.relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_client_id")
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_credit_id")
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    @EqualsAndHashCode.Exclude
    private LocalDateTime creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_status_history_id")
    private StatusHistory statusHistory;

}
