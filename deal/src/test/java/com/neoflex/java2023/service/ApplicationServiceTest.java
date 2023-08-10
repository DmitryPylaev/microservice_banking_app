package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.*;
import com.neoflex.java2023.model.json.PassportJSON;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import com.neoflex.java2023.model.jsonb.Passport;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;
import com.neoflex.java2023.service.abstraction.ApplicationService;
import com.neoflex.java2023.service.config.BaseTest;
import com.neoflex.java2023.service.mapper.CreditMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ApplicationServiceImpl.class})
@ComponentScan("com.neoflex.java2023")
@EnableAutoConfiguration
class ApplicationServiceTest extends BaseTest {
    @MockBean
    private FeignConveyor feignConveyor;
    @Autowired
    private ApplicationService applicationService;
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(300000);
    private static final Integer TERM = 18;
    private static final BigDecimal MONTHLY_PAYMENT = BigDecimal.valueOf(24970);
    private static final BigDecimal RATE = BigDecimal.valueOf(12.0);
    private static final BigDecimal PSK = BigDecimal.valueOf(14.978);
    private static final LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
            .amount(AMOUNT).term(TERM)
            .firstName("Vasiliy")
            .lastName("Petrov")
            .email("vas@gmail.com")
            .birthdate(LocalDate.parse("1977-08-16"))
            .passportSeries("5766")
            .passportNumber("576687")
            .build();
    private static final Passport passport = new Passport(PassportJSON.builder()
            .series(request.getPassportSeries())
            .number(request.getPassportNumber())
            .build());

    private static final Client client = Client.builder()
            .lastName(request.getLastName())
            .firstName(request.getFirstName())
            .middleName(request.getMiddleName())
            .birthDate(request.getBirthdate())
            .email(request.getEmail())
            .passport(passport)
            .build();

    private static final StatusHistoryJSON statusHistoryJSON = StatusHistoryJSON.builder()
            .status(ApplicationStatus.PREAPPROVAL)
            .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .changeType(ChangeType.AUTOMATIC).build();

    private static final Application application = Application.builder()
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .build();

    @BeforeAll
    static void prepareApplicationInstance() {
        List<StatusHistoryJSON> statusHistoryJSONList = new ArrayList<>();
        statusHistoryJSONList.add(statusHistoryJSON);
        StatusHistory statusHistory = new StatusHistory(statusHistoryJSONList);
        application.setStatusHistory(statusHistory);
    }

    @Test
    void createClient() {
        assertEquals(client, applicationService.createClient(request));
    }

    @Test
    void createApplication() {
        assertEquals(application, applicationService.createApplication(client));
    }

    @Test
    void getOffers() {
        List<LoanOfferDTO> expectedList = new ArrayList<>();
        LoanOfferDTO expectedDto = LoanOfferDTO.builder().build();
        expectedDto.setApplicationId(1L);
        expectedList.add(expectedDto);

        when(feignConveyor.getCreatedOffers(any())).thenReturn(expectedList);
        assertDoesNotThrow(() -> applicationService.getOffers(request, 1L));
        assertEquals(1L, applicationService.getOffers(request, 1L).get(0).getApplicationId());
        verify(feignConveyor, Mockito.times(2)).getCreatedOffers(any());
    }

    @Test
    void getCreditDtoFromRemote() {
        CreditDTO expectedCreditDTO = CreditDTO.builder()
                .amount(AMOUNT)
                .term(TERM)
                .monthlyPayment(MONTHLY_PAYMENT)
                .rate(RATE)
                .psk(PSK)
                .build();

        LoanOfferDTO expectedAppliedOffer = LoanOfferDTO.builder()
                .totalAmount(AMOUNT)
                .term(TERM)
                .monthlyPayment(MONTHLY_PAYMENT)
                .rate(RATE)
                .build();

        application.setAppliedOffer(expectedAppliedOffer);
        when(feignConveyor.getCalculatedCredit(any())).thenReturn(expectedCreditDTO);

        AtomicReference<CreditDTO> creditDTO = new AtomicReference<>();

        assertDoesNotThrow(() -> creditDTO.set(applicationService.getCreditDtoFromRemote(FinishRegistrationRequestDTO.builder().build(), application)));
        assertEquals(AMOUNT, creditDTO.get().getAmount());
        assertEquals(TERM, creditDTO.get().getTerm());
        assertEquals(MONTHLY_PAYMENT, creditDTO.get().getMonthlyPayment());
        assertEquals(RATE, creditDTO.get().getRate());
        assertEquals(PSK, creditDTO.get().getPsk());
        verify(feignConveyor, Mockito.times(1)).getCalculatedCredit(any());
    }

    @Test
    void mapCredit() {
        assertEquals(CreditStatus.CALCULATED, CreditMapper.mapCredit(CreditDTO.builder().build()).getCreditStatus());
    }
}