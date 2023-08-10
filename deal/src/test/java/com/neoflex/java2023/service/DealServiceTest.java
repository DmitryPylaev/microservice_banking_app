package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.enums.EmploymentPosition;
import com.neoflex.java2023.enums.EmploymentStatus;
import com.neoflex.java2023.model.json.PassportJSON;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import com.neoflex.java2023.model.jsonb.Passport;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.repository.ClientRepository;
import com.neoflex.java2023.repository.CreditRepository;
import com.neoflex.java2023.service.abstraction.ApplicationService;
import com.neoflex.java2023.service.abstraction.DealService;
import com.neoflex.java2023.service.config.BaseTest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {DealServiceImpl.class})
@ComponentScan("com.neoflex.java2023")
@EnableAutoConfiguration
@SuppressWarnings("unused")
class DealServiceTest extends BaseTest {
    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private CreditRepository creditRepository;

    @MockBean
    private ApplicationRepository applicationRepository;

    @Autowired
    private DealService dealService;

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

    private static final StatusHistoryJSON statusHistoryJSON = StatusHistoryJSON.builder().status(ApplicationStatus.PREAPPROVAL).time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).changeType(ChangeType.AUTOMATIC).build();

    private static final Application application = Application.builder()
            .client(client)
            .status(ApplicationStatus.PREAPPROVAL)
            .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .build();

    private static final LoanOfferDTO loanOfferDto = LoanOfferDTO.builder()
            .applicationId(1L)
            .totalAmount(AMOUNT)
            .term(TERM)
            .monthlyPayment(MONTHLY_PAYMENT)
            .rate(RATE)
            .build();

    private static final EmploymentDTO employmentDTO = EmploymentDTO.builder()
            .employmentStatus(EmploymentStatus.EMPLOYED)
            .salary(BigDecimal.valueOf(100000))
            .position(EmploymentPosition.WORKER)
            .workExperienceTotal(120)
            .workExperienceCurrent(120)
            .build();

    @BeforeAll
    static void prepareApplicationInstance() {
        List<StatusHistoryJSON> statusHistoryJSONList = new ArrayList<>();
        statusHistoryJSONList.add(statusHistoryJSON);
        StatusHistory statusHistory = new StatusHistory(statusHistoryJSONList);
        application.setStatusHistory(statusHistory);
    }

    @Test
    void acceptRequest() {
        List<LoanOfferDTO> expectedList = new ArrayList<>();
        expectedList.add(loanOfferDto);
        application.setAppliedOffer(loanOfferDto);

        when(clientRepository.save(any())).thenReturn(client);
        when(applicationRepository.save(any())).thenReturn(application);
        when(applicationService.getOffers(any(LoanApplicationRequestDTO.class), anyLong())).thenReturn(expectedList);

        assertDoesNotThrow(() -> dealService.acceptRequest(request));
        LoanOfferDTO result = dealService.acceptRequest(request).get(0);

        assertEquals(1L, result.getApplicationId());
        assertEquals(AMOUNT, result.getTotalAmount());
        assertEquals(TERM, result.getTerm());
        assertEquals(RATE, result.getRate());
        assertEquals(MONTHLY_PAYMENT, result.getMonthlyPayment());
        verify(applicationService, Mockito.times(2)).createClient(any());
        verify(applicationService, Mockito.times(2)).createApplication(any());
        verify(applicationService, Mockito.times(2)).getOffers(any(LoanApplicationRequestDTO.class), anyLong());
    }

    @Test
    void updateApplication() {
        when(applicationRepository.save(any())).thenReturn(application);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        assertEquals(ApplicationStatus.APPROVED, dealService.updateApplication(loanOfferDto).getStatus());

        when(applicationRepository.findById(10L)).thenReturn(Optional.empty());
        loanOfferDto.setApplicationId(10L);
        assertEquals(Application.builder().build(), dealService.updateApplication(loanOfferDto));

        application.setStatus(ApplicationStatus.PREAPPROVAL);
        loanOfferDto.setApplicationId(1L);
    }

    @Test
    void calculate() {
        CreditDTO expectedCreditDTO = CreditDTO.builder()
                .amount(AMOUNT)
                .term(TERM)
                .monthlyPayment(MONTHLY_PAYMENT)
                .rate(RATE)
                .psk(PSK)
                .build();

        FinishRegistrationRequestDTO request = FinishRegistrationRequestDTO.builder()
                .employment(employmentDTO)
                .build();

        when(applicationService.getCreditDtoFromRemote(any(FinishRegistrationRequestDTO.class), any(Application.class))).thenReturn(expectedCreditDTO);

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(application));
        assertEquals(expectedCreditDTO, dealService.finishCalculateCredit(request, 1L));

        when(applicationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertEquals(CreditDTO.builder().build(), dealService.finishCalculateCredit(FinishRegistrationRequestDTO.builder().build(), 1L));
    }
}