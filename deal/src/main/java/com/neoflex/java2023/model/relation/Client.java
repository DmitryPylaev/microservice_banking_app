package com.neoflex.java2023.model.relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.enums.Gender;
import com.neoflex.java2023.enums.MaritalStatus;
import com.neoflex.java2023.model.jsonb.Employment;
import com.neoflex.java2023.model.jsonb.Passport;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "client")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "martial_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_passport_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Passport passport;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_employment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employment employment;

    @Column(name = "account")
    private String account;
}
