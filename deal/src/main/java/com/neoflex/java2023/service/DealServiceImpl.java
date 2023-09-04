package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.enums.CreditStatus;
import com.neoflex.java2023.enums.Theme;
import com.neoflex.java2023.model.*;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.repository.ClientRepository;
import com.neoflex.java2023.repository.CreditRepository;
import com.neoflex.java2023.service.abstraction.*;
import com.neoflex.java2023.service.mapper.CreditMapper;
import com.neoflex.java2023.service.mapper.EmploymentMapper;
import com.neoflex.java2023.util.CustomLogger;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class DealServiceImpl implements DealService {
    private final BigDecimal denyRate;
    private final ApplicationBuildService applicationBuildService;
    private final ConveyorAccessService conveyorAccessService;
    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final EmploymentMapper employmentMapper;
    private final KafkaService kafkaService;
    private final DocumentService documentService;

    public DealServiceImpl(@Value("${denyRate}") Integer denyRate,
                           ApplicationBuildService applicationBuildService,
                           ConveyorAccessService conveyorAccessService,
                           ClientRepository clientRepository,
                           ApplicationRepository applicationRepository,
                           CreditRepository creditRepository,
                           CreditMapper creditMapper,
                           EmploymentMapper employmentMapper,
                           KafkaService kafkaService,
                           DocumentService documentService) {
        this.denyRate = BigDecimal.valueOf(denyRate);
        this.applicationBuildService = applicationBuildService;
        this.conveyorAccessService = conveyorAccessService;
        this.clientRepository = clientRepository;
        this.applicationRepository = applicationRepository;
        this.creditRepository = creditRepository;
        this.creditMapper = creditMapper;
        this.employmentMapper = employmentMapper;
        this.kafkaService = kafkaService;
        this.documentService = documentService;
    }

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
        actualizeApplicationStatus(ApplicationStatus.APPROVED, application);
        kafkaService.generateEmail(Theme.FINISH_REGISTRATION, application);
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public CreditDTO finishCalculateCredit(FinishRegistrationRequestDTO request, Long applicationId) {
        CustomLogger.logInfoClassAndMethod();
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) return CreditDTO.builder().build();
        Application application = optionalApplication.get();
        CreditDTO creditDto = conveyorAccessService.getCreditDtoFromRemote(request, application);
        actualizeClient(request.getEmployment(), application);
        actualizeCredit(CreditStatus.CALCULATED, creditMapper.mapCredit(creditDto), application);
        deniedCheck(application);
        documentService.createDocument(application);
        return creditDto;
    }

    @Override
    public void sendMessage(ApplicationStatus applicationStatus, Theme theme, Long applicationId) {
        CustomLogger.logInfoClassAndMethod();
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) return;
        Application application = optionalApplication.get();
        actualizeApplicationStatus(applicationStatus, application);
        kafkaService.generateEmail(theme, application);
    }

    private void deniedCheck(Application application) {
        if (Objects.equals(application.getCredit().getRate(), denyRate)) {
            actualizeApplicationStatus(ApplicationStatus.CC_DENIED, application);
            kafkaService.generateEmail(Theme.APPLICATION_DENIED, application);
        } else {
            actualizeApplicationStatus(ApplicationStatus.CC_APPROVED, application);
            kafkaService.generateEmail(Theme.CREATE_DOCUMENTS, application);
        }
    }

    private void actualizeApplicationStatus(ApplicationStatus applicationStatus, Application application) {
        CustomLogger.logInfoClassAndMethod();
        application.setStatus(applicationStatus);
        if (application.getStatus() == ApplicationStatus.CREDIT_ISSUED)
            actualizeCredit(CreditStatus.ISSUED, application.getCredit(), application);
        actualizeApplicationStatusHistory(applicationStatus, application);
    }

    private void actualizeApplicationStatusHistory(ApplicationStatus applicationStatus, Application application) {
        CustomLogger.logInfoClassAndMethod();
        List<StatusHistoryElement> statusHistoryElementList = application.getStatusHistory();
        StatusHistoryElement statusHistoryElement = StatusHistoryElement.builder()
                .status(applicationStatus)
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

    private void actualizeCredit(CreditStatus creditStatus, Credit credit, Application application) {
        credit.setCreditStatus(creditStatus);
        application.setCredit(creditRepository.save(credit));
        applicationRepository.save(application);
    }
}
