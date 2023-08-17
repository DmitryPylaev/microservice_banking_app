package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.enums.CreditStatus;
import com.neoflex.java2023.model.*;
import com.neoflex.java2023.model.StatusHistoryElement;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.repository.ClientRepository;
import com.neoflex.java2023.repository.CreditRepository;
import com.neoflex.java2023.service.abstraction.ApplicationBuildService;
import com.neoflex.java2023.service.abstraction.ConveyorAccessService;
import com.neoflex.java2023.service.abstraction.DealService;
import com.neoflex.java2023.service.mapper.CreditMapper;
import com.neoflex.java2023.service.mapper.EmploymentMapper;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class DealServiceImpl implements DealService {

    private ApplicationBuildService applicationBuildService;
    private ConveyorAccessService conveyorAccessService;
    private ClientRepository clientRepository;
    private ApplicationRepository applicationRepository;
    private CreditRepository creditRepository;
    private CreditMapper creditMapper;
    private EmploymentMapper employmentMapper;

    @Override
    @Transactional
    public List<LoanOfferDTO> acceptRequest(LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        Client client = clientRepository.save(applicationBuildService.createClient(request));
        Application application = applicationRepository.save(applicationBuildService.createApplication(client));
        return conveyorAccessService.getOffers(request, application.getId());
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

    @Override
    @Transactional
    public CreditDTO finishCalculateCredit(FinishRegistrationRequestDTO request, Long applicationId) {
        CustomLogger.logInfoClassAndMethod();
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) return CreditDTO.builder().build();
        else {
            Application application = optionalApplication.get();
            CreditDTO creditDto = conveyorAccessService.getCreditDtoFromRemote(request, application);
            actualizeClient(request.getEmployment(), application);
            actualizeCredit(creditDto, application);
            return creditDto;
        }
    }

    private void updateStatusHistory(Application application) {
        CustomLogger.logInfoClassAndMethod();
        List<StatusHistoryElement> statusHistoryElementList = application.getStatusHistoryElement();
        StatusHistoryElement statusHistoryElement = StatusHistoryElement.builder()
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .changeType(ChangeType.AUTOMATIC)
                .build();
        statusHistoryElementList.add(statusHistoryElement);
    }

    private void actualizeClient(EmploymentDTO employmentDto, Application application) {
        Client client = application.getClient();
        Employment employment = employmentMapper.mapEmploymentJSON(employmentDto);
        client.setEmployment(employment);
        clientRepository.save(client);
    }

    private void actualizeCredit(CreditDTO creditDto, Application application){
        Credit credit = creditMapper.mapCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCredit(creditRepository.save(credit));
        applicationRepository.save(application);
    }
}
