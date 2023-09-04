package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "deal", url = "http://localhost:8080/deal")
public interface FeignDeal {

    @PostMapping("application")
    List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("offer")
    void specifyApplication(@RequestBody LoanOfferDTO request);

}
