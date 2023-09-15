package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.model.Application;

import java.util.List;

public interface AdminService {
    Application findApplicationById(Long id);

    List<Application> findPageApplication();
}
