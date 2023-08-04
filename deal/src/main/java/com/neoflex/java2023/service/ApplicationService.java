package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;
import com.neoflex.java2023.model.jsonb.Passport;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import com.neoflex.java2023.model.json.PassportJSON;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import com.neoflex.java2023.repository.PassportRepository;
import com.neoflex.java2023.repository.StatusHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ApplicationService {
    private PassportRepository passportRepository;
    private StatusHistoryRepository statusHistoryRepository;
    private FeignConveyor feignConveyor;

    public Client createClient(LoanApplicationRequestDTO request) {
        Passport passport = passportRepository.save(new Passport(PassportJSON.builder()
                .series(request.getPassportSeries())
                .number(request.getPassportNumber())
                .build()));

        return Client.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .birthDate(request.getBirthdate())
                .email(request.getEmail())
                .passport(passport)
                .build();
    }

    public Application createApplication(Client client) {
        StatusHistoryJSON statusHistoryJSON = StatusHistoryJSON.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        List<StatusHistoryJSON> statusHistoryJSONList = new ArrayList<>();
        statusHistoryJSONList.add(statusHistoryJSON);
        StatusHistory statusHistory = statusHistoryRepository.save(new StatusHistory(statusHistoryJSONList));

        return Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .statusHistory(statusHistory)
                .build();
    }

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request, long id) {
        List<LoanOfferDTO> offers = feignConveyor.getCreatedOffers(request);
        offers.forEach(o -> o.setApplicationId(id));
        return offers;
    }

    public CreditDTO getCredit(FinishRegistrationRequestDTO request, Application application) {
        Client client = application.getClient();
        LoanOfferDTO appliedOffer = application.getAppliedOffer();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(appliedOffer.getTotalAmount())
                .term(appliedOffer.getTerm())
                .isInsuranceEnabled(appliedOffer.getIsInsuranceEnabled())
                .isSalaryClient(appliedOffer.getIsSalaryClient())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getJson().getSeries())
                .passportNumber(client.getPassport().getJson().getNumber())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .dependentAmount(request.getDependentAmount())
                .passportIssueDate(request.getPassportIssueDate())
                .passportIssueBranch(request.getPassportIssueBranch())
                .employment(request.getEmployment())
                .account(request.getAccount())
                .build();
        return feignConveyor.getCredit(scoringDataDTO);
    }
}
