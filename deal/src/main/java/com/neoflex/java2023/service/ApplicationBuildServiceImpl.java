package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.*;
import com.neoflex.java2023.enums.ApplicationStatus;
import com.neoflex.java2023.enums.ChangeType;
import com.neoflex.java2023.model.*;
import com.neoflex.java2023.model.StatusHistoryElement;
import com.neoflex.java2023.service.abstraction.ApplicationBuildService;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ApplicationBuildServiceImpl implements ApplicationBuildService {

    @Override
    public Client createClient(LoanApplicationRequestDTO request) {
        CustomLogger.logInfoClassAndMethod();
        Passport passport = Passport.builder()
                .series(request.getPassportSeries())
                .number(request.getPassportNumber())
                .build();

        return Client.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .birthDate(request.getBirthdate())
                .email(request.getEmail())
                .passport(passport)
                .build();
    }

    @Override
    public Application createApplication(Client client) {
        CustomLogger.logInfoClassAndMethod();
        StatusHistoryElement statusHistoryElement = StatusHistoryElement.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .changeType(ChangeType.AUTOMATIC)
                .build();
        List<StatusHistoryElement> statusHistory = new ArrayList<>();
        statusHistory.add(statusHistoryElement);

        return Application.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistory(statusHistory)
                .build();
    }
}
