package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.entity.CarbonRecord;
import com.corporate.travel.repository.CarbonRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SustainabilityService {
    private static final Logger log = LoggerFactory.getLogger(SustainabilityService.class);

    public SustainabilityService(CarbonRecordRepository carbonRecordRepository) {
        this.carbonRecordRepository = carbonRecordRepository;
    }


    private final CarbonRecordRepository carbonRecordRepository;

    public BigDecimal calculateCarbonForFlight(double distanceKm, int passengerCount) {
        // approx 0.115 kg CO2 per passenger-km
        return BigDecimal.valueOf(distanceKm * 0.115 * passengerCount);
    }

    public List<CarbonRecord> getUserCarbonHistory(Long userId) {
        return carbonRecordRepository.findByUserId(userId);
    }
}
