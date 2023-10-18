package com.neoflex.java.controller;

import com.neoflex.java.dto.*;
import com.neoflex.java.model.Application;
import com.neoflex.java.service.abstraction.DealService;
import com.neoflex.java.service.abstraction.AccountService;
import com.neoflex.java.util.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер микросервиса сделки
 */
@RestController
@RequestMapping("/deal")
@AllArgsConstructor
@SuppressWarnings("unused")
@Tag(name = "DealController", description = "Контроллер микросервиса сделки")
public class DealController {
    private DealService dealService;
    private AccountService accountService;

    @Operation(summary = "Расчёт возможных условий кредита")
    @PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoanOfferDTO> createOffers(@RequestBody LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return dealService.acceptRequest(request);
    }

    @Operation(summary = "Выбор одного из предложений")
    @PutMapping(value = "/offer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Application specifyApplication(@RequestBody LoanOfferDTO request) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return dealService.updateApplication(request);
    }

    @Operation(summary = "Завершение регистрации + полный подсчёт кредита")
    @PutMapping(value = "/calculate/{applicationId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditDTO finishCalculateCredit(@RequestBody FinishRegistrationRequestDTO request, @RequestParam long id) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return dealService.finishCalculateCredit(request, id);
    }

    @Operation(summary = "Запрос авторизации")
    @GetMapping(value = "/account/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDTO getAccountDTO(@RequestParam String username) {
        CustomLogger.logInfoClassAndMethod();
        return accountService.getAccountDTO(username);
    }
}
