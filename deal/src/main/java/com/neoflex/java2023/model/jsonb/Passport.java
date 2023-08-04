package com.neoflex.java2023.model.jsonb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.model.json.PassportJSON;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Table(name = "passport")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class Passport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passport_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private PassportJSON json;

    public Passport(PassportJSON json) {
        this.json = json;
    }
}
