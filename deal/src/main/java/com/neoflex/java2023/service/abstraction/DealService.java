package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.FinishRegistrationRequestDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.model.relation.Application;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> acceptRequest(LoanApplicationRequestDTO request);

    Application updateApplication(LoanOfferDTO request);

    CreditDTO finishCalculateCredit(FinishRegistrationRequestDTO request, Long applicationId);
}
