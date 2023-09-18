package com.neoflex.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Passport implements Serializable {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
