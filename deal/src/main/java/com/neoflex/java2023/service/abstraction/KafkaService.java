package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.enums.EmailMessageTheme;
import com.neoflex.java2023.model.Application;

public interface KafkaService {
    void generateEmail(EmailMessageTheme emailMessageTheme, Application application);
}
