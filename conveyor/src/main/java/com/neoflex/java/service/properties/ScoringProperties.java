package com.neoflex.java.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@ConfigurationProperties(prefix = "scoring")
@ConfigurationPropertiesScan("application.properties")
public class ScoringProperties {
    private final Double baseRate;
    private final Integer denyRate;
    private final Integer insurancePrice;
    private final Double insuranceRateDiscount;
    private final Double salaryClientRateDiscount;
    private final Integer minAge;
    private final Integer maxAge;
    private final Integer salaryMinCoefficient;
    private final Integer workExperienceTotalMin;
    private final Integer workExperienceCurrentMin;
    private final Double singleRateDiscount;
    private final Double marriedRateDiscount;
    private final Double middleRateDiscount;
    private final Double seniorRateDiscount;
    private final Double dependentAmountRateDiscount;
}
