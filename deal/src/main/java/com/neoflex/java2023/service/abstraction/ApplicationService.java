package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.FinishRegistrationRequestDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;

import java.util.List;

public interface ApplicationService {
    Client createClient(LoanApplicationRequestDTO request);

    Application createApplication(Client client);

    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request, long id);

    CreditDTO getCreditDtoFromRemote(FinishRegistrationRequestDTO request, Application application);
}
