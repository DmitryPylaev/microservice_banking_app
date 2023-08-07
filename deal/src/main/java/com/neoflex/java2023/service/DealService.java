package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.CreditDTO;
import com.neoflex.java2023.dto.FinishRegistrationRequestDTO;
import com.neoflex.java2023.dto.LoanApplicationRequestDTO;
import com.neoflex.java2023.dto.LoanOfferDTO;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;
import com.neoflex.java2023.model.relation.Credit;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.repository.ClientRepository;
import com.neoflex.java2023.repository.CreditRepository;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DealService {
    private ApplicationService applicationService;
    private ClientRepository clientRepository;
    private ApplicationRepository applicationRepository;
    private CreditRepository creditRepository;


    public List<LoanOfferDTO> acceptRequest(LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        Client client = clientRepository.save(applicationService.createClient(request));
        Application application = applicationRepository.save(applicationService.createApplication(client));
        return applicationService.getOffers(request, application.getId());
    }

    public Application udpateApplication(LoanOfferDTO request) {
        Long id = request.getApplicationId();
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isEmpty()) return Application.builder().build();
        Application application = optionalApplication.get();
        application.setStatus(ApplicationStatus.APPROVED);
        application.setAppliedOffer(request);
        statusHistoryUpdate(application);
        return applicationRepository.save(application);
    }

    private void statusHistoryUpdate(Application application) {
        StatusHistory statusHistory = application.getStatusHistory();
        List<StatusHistoryJSON> statusHistoryJSONList = statusHistory.getJson();
        StatusHistoryJSON statusHistoryJSON = StatusHistoryJSON.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryJSONList.add(statusHistoryJSON);
        application.setStatusHistory(statusHistory);
    }

    public CreditDTO calculate(FinishRegistrationRequestDTO request, Long applicationId) {
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) return CreditDTO.builder().build();
        else {
            Application application = optionalApplication.get();
            CreditDTO creditDto = applicationService.getCreditDtoFromRemote(request, application);
            Credit credit = creditRepository.save(applicationService.mapCredit(creditDto));
            application.setCredit(credit);
            applicationRepository.save(application);
            return creditDto;
        }
    }

}
