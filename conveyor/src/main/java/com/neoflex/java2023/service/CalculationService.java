package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.ScoringDataDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CalculationService {

    public CreditDTO scoring(ScoringDataDTO dto) {
        return CreditDTO.builder().build();
    }

}
