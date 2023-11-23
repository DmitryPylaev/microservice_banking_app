package com.neoflex.java.service;

import com.neoflex.java.dto.FindApplicationDTO;
import com.neoflex.java.model.Application;
import com.neoflex.java.repository.ApplicationRepository;
import com.neoflex.java.service.abstraction.AdminService;
import com.neoflex.java.service.filter.ApplicationSpecification;
import com.neoflex.java.util.CustomLogger;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private static final int PAGE_SIZE = 20;

    private final ApplicationRepository applicationRepository;

    @Override
    public Application findApplicationById(Long id) {
        CustomLogger.logInfoClassAndMethod();
        return applicationRepository.findById(id).orElseGet(() -> Application.builder().build());
    }

    @Override
    public List<Application> findPageApplication() {
        CustomLogger.logInfoClassAndMethod();
        return applicationRepository.findAll(PageRequest.of(0, PAGE_SIZE)).getContent();
    }

    @Override
    public List<Application> findApplicationByFilter(FindApplicationDTO request) {
        CustomLogger.logInfoClassAndMethod();
        return applicationRepository.findAll(ApplicationSpecification.getSpec(request), PageRequest.of(0, PAGE_SIZE)).toList();
    }
}
