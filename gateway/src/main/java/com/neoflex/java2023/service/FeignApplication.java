package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "application", url = "http://localhost:8082/application")
public interface FeignApplication {

    @PostMapping
    List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("offer")
    void specifyApplication(@RequestBody LoanOfferDTO request);

}
