package com.neoflex.java2023.controller;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.service.abstraction.PrescoringService;
import com.neoflex.java2023.util.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер микросервиса заявки
 */
@Controller
@RequestMapping("/application")
@AllArgsConstructor
@SuppressWarnings("unused")
@Tag(name = "ApplicationController", description = "Контроллер микросервиса заявки")
public class ApplicationController {
    private PrescoringService prescoringService;

    @Operation(summary = "Прескоринг + Расчёт возможных условий кредита")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LoanOfferDTO> createOffers(@RequestBody LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return prescoringService.getPreparedOffers(request);
    }

    @Operation(summary = "Выбор одного из предложений")
    @PutMapping(value = "/offer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> specifyApplication(@RequestBody LoanOfferDTO request) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return prescoringService.executeSpecifyApplicationRequest(request);
    }
}
