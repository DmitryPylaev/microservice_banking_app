package com.neoflex.java.controller;

import com.neoflex.java.dto.FindApplicationDTO;
import com.neoflex.java.model.Application;
import com.neoflex.java.service.abstraction.AdminService;
import com.neoflex.java.util.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер микросервиса сделки
 */
@RestController
@RequestMapping("/deal/admin/application")
@AllArgsConstructor
@SuppressWarnings("unused")
@Tag(name = "AdminController", description = "Контроллер администратора")
public class AdminController {
    private AdminService adminService;

    @Operation(summary = "Получить заявку по id")
    @PostMapping(value = "/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Application getApplication(@RequestParam long id) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(id);
        return adminService.findApplicationById(id);
    }

    @Operation(summary = "Получить все заявки")
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> getAllApplications() {
        CustomLogger.logInfoClassAndMethod();
        return adminService.findPageApplication();
    }

    @Operation(summary = "Поиск заявки по статусу, супружескому статусу заемщика, полу заемщика, " +
            "является ли зарплатным клиентом или имеет страховку," +
            "более какой-то суммы и срока")
    @PostMapping(value = "/findByFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Application> findApplicationByFilter(@RequestBody FindApplicationDTO request) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(request);
        return adminService.findApplicationByFilter(request);
    }
}
