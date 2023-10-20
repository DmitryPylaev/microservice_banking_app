package com.neoflex.java.service.abstraction;

import com.neoflex.java.dto.EmailMessageTheme;
import com.neoflex.java.model.Application;

public interface KafkaService {
    void generateEmail(EmailMessageTheme emailMessageTheme, Application application);
}
