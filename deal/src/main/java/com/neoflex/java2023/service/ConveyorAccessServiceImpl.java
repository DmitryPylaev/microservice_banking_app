package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.model.Application;
import com.neoflex.java2023.model.Client;
import com.neoflex.java2023.service.abstraction.ConveyorAccessService;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConveyorAccessServiceImpl implements ConveyorAccessService {
    private FeignConveyor feignConveyor;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request, long id) {
        CustomLogger.logInfoClassAndMethod();
        List<LoanOfferDTO> offers = feignConveyor.getCreatedOffers(request);
        offers.forEach(o -> o.setApplicationId(id));
        return offers;
    }

    @Override
    public CreditDTO getCreditDtoFromRemote(FinishRegistrationRequestDTO request, Application application) {
        CustomLogger.logInfoClassAndMethod();
        Client client = application.getClient();
        LoanOfferDTO appliedOffer = application.getAppliedOffer();
        return feignConveyor.getCalculatedCredit(ScoringDataDTO.builder()
                .amount(appliedOffer.getTotalAmount())
                .term(appliedOffer.getTerm())
                .isInsuranceEnabled(appliedOffer.getIsInsuranceEnabled())
                .isSalaryClient(appliedOffer.getIsSalaryClient())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .dependentAmount(request.getDependentAmount())
                .passportIssueDate(request.getPassportIssueDate())
                .passportIssueBranch(request.getPassportIssueBranch())
                .employment(request.getEmployment())
                .account(request.getAccount())
                .build());
    }
}
