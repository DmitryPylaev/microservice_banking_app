package com.neoflex.java.service.abstraction;

import com.neoflex.java.model.Application;

import java.util.List;

public interface AdminService {
    Application findApplicationById(Long id);

    List<Application> findPageApplication();
}
