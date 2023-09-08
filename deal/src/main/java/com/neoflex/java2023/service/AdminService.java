package com.neoflex.java2023.service;

import com.neoflex.java2023.model.Application;
import com.neoflex.java2023.repository.ApplicationRepository;
import com.neoflex.java2023.util.CustomLogger;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class AdminService {
    private final ApplicationRepository applicationRepository;
    private static final int pageSize = 1000;

    public Application findApplicationById(Long id) {
        CustomLogger.logInfoClassAndMethod();
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        return optionalApplication.orElseGet(() -> Application.builder().build());
    }

    public List<Application> findPageApplication() {
        CustomLogger.logInfoClassAndMethod();
        Pageable firstPage = PageRequest.of(0, pageSize);
        Page<Application> applicationPage = applicationRepository.findAll(firstPage);
        return applicationPage.getContent();
    }
}
