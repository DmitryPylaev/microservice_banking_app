package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "conveyor", url = "${conveyor.destination}")
public interface FeignConveyor {

    @PostMapping("offers")
    List<LoanOfferDTO> getCreatedOffers(@RequestBody LoanApplicationRequestDTO request);

    @PostMapping("calculation")
    CreditDTO getCalculatedCredit(@RequestBody ScoringDataDTO request);
}
