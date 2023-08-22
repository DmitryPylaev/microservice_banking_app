package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrescoringService {
    List<LoanOfferDTO> getPreparedOffers(LoanApplicationRequestDTO request);

    ResponseEntity<String> executeSpecifyApplicationRequest(LoanOfferDTO request);
}
