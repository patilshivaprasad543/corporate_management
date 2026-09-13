package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.RiskAlert;
import com.corporate.travel.entity.enums.RiskLevel;
import com.corporate.travel.repository.RiskAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskManagementService {
    private static final Logger log = LoggerFactory.getLogger(RiskManagementService.class);

    public RiskManagementService(RiskAlertRepository riskAlertRepository) {
        this.riskAlertRepository = riskAlertRepository;
    }


    private final RiskAlertRepository riskAlertRepository;

    public List<RiskAlert> getActiveAlerts() {
        return riskAlertRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    public RiskAlert createAlert(String dest, String countryCode, RiskLevel level, String title, String desc, String category) {
        return riskAlertRepository.save(RiskAlert.builder()
                .destination(dest)
                .countryCode(countryCode)
                .riskLevel(level)
                .title(title)
                .description(desc)
                .category(category)
                .active(true)
                .build());
    }
}
