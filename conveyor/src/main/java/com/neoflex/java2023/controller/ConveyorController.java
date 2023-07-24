package com.neoflex.java2023.controller;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.dto.ScoringDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conveyor")
@AllArgsConstructor
@Log4j2
@Tag(name = "ConveyorController", description = "Контроллер конвейра")
public class ConveyorController {
    @Operation(summary = "Расчёт возможных условий кредита")
    @PostMapping(value = "/offers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoanOfferDTO> offers(@RequestBody LoanApplicationRequestDTO dto) {
        log.info("В методе контроллера Conveyor::offers");
        return new ArrayList<>();
    }

    @Operation(summary = "Валидация присланных данных + скоринг данных + полный расчет параметров кредита")
    @PostMapping(value = "/calculation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditDTO calculation(@RequestBody ScoringDataDTO dto) {
        log.info("В методе контроллера Conveyor::calculation");
        return new CreditDTO();
    }
}
