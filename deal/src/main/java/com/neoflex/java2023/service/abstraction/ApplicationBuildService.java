package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.model.Application;
import com.neoflex.java2023.model.Client;

public interface ApplicationBuildService {
    Client createClient(LoanApplicationRequestDTO request);

    Application createApplication(Client client);
}
