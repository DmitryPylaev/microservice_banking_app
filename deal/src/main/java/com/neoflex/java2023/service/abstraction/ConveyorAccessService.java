package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.FinishRegistrationRequestDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.model.Application;

import java.util.List;

public interface ConveyorAccessService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request, long id);

    CreditDTO getCreditDtoFromRemote(FinishRegistrationRequestDTO request, Application application);
}
