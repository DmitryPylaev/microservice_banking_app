package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;

public interface ValidateService {
    boolean validatePrescoringRequest(LoanApplicationRequestDTO request);
}
