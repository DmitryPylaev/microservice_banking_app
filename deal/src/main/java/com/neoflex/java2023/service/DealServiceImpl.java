package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.model.json.StatusHistoryJSON;
import com.neoflex.java2023.model.jsonb.Employment;
import com.neoflex.java2023.model.jsonb.StatusHistory;
import com.neoflex.java2023.model.relation.Application;
import com.neoflex.java2023.model.relation.Client;
import com.neoflex.java2023.model.relation.Credit;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.repository.ClientRepository;
import com.neoflex.java2023.repository.CreditRepository;
import com.neoflex.java2023.repository.EmploymentRepository;
import com.neoflex.java2023.service.abstraction.ApplicationService;
import com.neoflex.java2023.service.abstraction.DealService;
import com.neoflex.java2023.service.mapper.CreditMapper;
import com.neoflex.java2023.service.mapper.EmploymentMapper;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DealServiceImpl implements DealService {

    private ApplicationService applicationService;
    private ClientRepository clientRepository;
    private ApplicationRepository applicationRepository;
    private CreditRepository creditRepository;
    private EmploymentRepository employmentRepository;


    @Override
    public List<LoanOfferDTO> acceptRequest(LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        Client client = clientRepository.save(applicationService.createClient(request));
        Application application = applicationRepository.save(applicationService.createApplication(client));
        return applicationService.getOffers(request, application.getId());
    }

    @Override
    public Application updateApplication(LoanOfferDTO request) {
        CustomLogger.logInfoClassAndMethod();
        Long id = request.getApplicationId();
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isEmpty()) return Application.builder().build();
        Application application = optionalApplication.get();
        application.setAppliedOffer(request);
        application.setStatus(ApplicationStatus.APPROVED);
        updateStatusHistory(application);
        return applicationRepository.save(application);
    }

    private void updateStatusHistory(Application application) {
        CustomLogger.logInfoClassAndMethod();
        StatusHistory statusHistory = application.getStatusHistory();
        List<StatusHistoryJSON> statusHistoryJSONList = statusHistory.getJson();
        StatusHistoryJSON statusHistoryJSON = StatusHistoryJSON.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryJSONList.add(statusHistoryJSON);
    }

    @Override
    public CreditDTO finishCalculateCredit(FinishRegistrationRequestDTO request, Long applicationId) {
        CustomLogger.logInfoClassAndMethod();
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) return CreditDTO.builder().build();
        else {
            Application application = optionalApplication.get();
            CreditDTO creditDto = applicationService.getCreditDtoFromRemote(request, application);
            actualizeClient(request.getEmployment(), application);
            actualizeCredit(creditDto, application);
            return creditDto;
        }
    }

    private void actualizeClient(EmploymentDTO employmentDto, Application application) {
        Client client = application.getClient();
        Employment employment = new Employment(EmploymentMapper.mapEmploymentJSON(employmentDto));
        client.setEmployment(employmentRepository.save(employment));
        clientRepository.save(client);
    }

    private void actualizeCredit(CreditDTO creditDto, Application application){
        Credit credit = creditRepository.save(CreditMapper.mapCredit(creditDto));
        application.setCredit(credit);
        applicationRepository.save(application);
    }
}
