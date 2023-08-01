package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.dto.ScoringDataDTO;

import java.util.List;

public interface OffersService {
    List<LoanOfferDTO> createPrescoringOffers(LoanApplicationRequestDTO request);

    CreditDTO createCreditOffer(ScoringDataDTO request);
}
