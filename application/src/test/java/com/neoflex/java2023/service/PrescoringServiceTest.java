package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.service.abstraction.PrescoringService;
import com.neoflex.java2023.service.abstraction.ValidateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {PrescoringServiceImpl.class})
@ComponentScan("com.neoflex.java2023")
@ExtendWith(OutputCaptureExtension.class)
class PrescoringServiceTest {

    @Autowired
    private PrescoringService prescoringService;

    @MockBean
    private ValidateService validateService;

    @MockBean
    private FeignDeal feignDeal;

    @Test
    void getPreparedOffers() {
        LoanApplicationRequestDTO dto = LoanApplicationRequestDTO.builder()
                .build();
        List<LoanOfferDTO> expectedList = new ArrayList<>();
        expectedList.add(LoanOfferDTO.builder()
                .totalAmount(BigDecimal.TEN)
                .build());

        when(feignDeal.getOffers(any())).thenReturn(expectedList);
        when(validateService.validatePrescoringRequest(any())).thenReturn(true);
        List<LoanOfferDTO> resultList = prescoringService.getPreparedOffers(dto);
        assertEquals(resultList.size(), 1);
        assertEquals(resultList.get(0).getTotalAmount(), BigDecimal.TEN);

        when(validateService.validatePrescoringRequest(any())).thenReturn(false);
        assertEquals(prescoringService.getPreparedOffers(dto).size(), 0);
    }

    @Test
    void executeSpecifyApplicationRequest() {
        LoanOfferDTO dto = LoanOfferDTO.builder().build();

        assertEquals(prescoringService.executeSpecifyApplicationRequest(dto), ResponseEntity.ok("Заявка обработана"));

        assertThrows(Exception.class, () -> doThrow(new Exception()).when(feignDeal).specifyApplication(any()));
        assertEquals(prescoringService.executeSpecifyApplicationRequest(dto), ResponseEntity.badRequest().body("""
                Ошибка сети\s
                Checked exception is invalid for this method!
                Invalid: java.lang.Exception"""));
    }
}