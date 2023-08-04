package com.neoflex.java2023.model.jsonb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "status_history")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class StatusHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_history_id_seq")
    @EqualsAndHashCode.Exclude
    private long id;

    @Column(name = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatusHistoryJSON> json;

    public StatusHistory(List<StatusHistoryJSON> json) {
        this.json = json;
    }
}
