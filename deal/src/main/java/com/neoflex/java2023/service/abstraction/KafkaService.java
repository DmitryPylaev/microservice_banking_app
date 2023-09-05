package com.neoflex.java2023.service.abstraction;

import com.neoflex.java2023.enums.Theme;
import com.neoflex.java2023.model.Application;

public interface KafkaService {
    void generateEmail(Theme theme, Application application);
}
