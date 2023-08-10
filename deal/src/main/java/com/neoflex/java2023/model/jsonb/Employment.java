package com.neoflex.java2023.model.jsonb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.model.json.EmploymentJSON;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Table(name = "employment")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
public class Employment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employment_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentJSON json;

    public Employment(EmploymentJSON json) {
        this.json = json;
    }
}
