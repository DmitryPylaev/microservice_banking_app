package com.neoflex.java2023.controller;

import com.neoflex.java2023.model.Application;
import com.neoflex.java2023.service.AdminService;
import com.neoflex.java2023.util.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер микросервиса сделки
 */
@RestController
@RequestMapping("/deal/admin/application")
@AllArgsConstructor
@Log4j2
@SuppressWarnings("unused")
@Tag(name = "AdminController", description = "Контроллер администратора")
public class AdminController {
    private AdminService adminService;

    @Operation(summary = "Получить заявку по id")
    @PostMapping(value = "/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Application getApplication(@RequestParam long id) {
        CustomLogger.logInfoClassAndMethod();
        return adminService.findApplicationById(id);
    }

    @Operation(summary = "Получить все заявки")
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> getAllApplications() {
        CustomLogger.logInfoClassAndMethod();
        return adminService.findPageApplication();
    }
}
