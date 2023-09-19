package com.neoflex.java.service.abstraction;

import com.neoflex.java.dto.LoanApplicationRequestDTO;

public interface ValidateService {
    boolean validatePrescoringRequest(LoanApplicationRequestDTO request);
}
