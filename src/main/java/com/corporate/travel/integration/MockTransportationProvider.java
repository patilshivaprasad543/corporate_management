package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockTransportationProvider implements TransportationProvider {
    private static final Logger log = LoggerFactory.getLogger(MockTransportationProvider.class);


    @Override
    public List<TravelSearchDto.TransportResult> searchTransport(String origin, String destination) {
        List<TravelSearchDto.TransportResult> list = new ArrayList<>();
        list.add(TravelSearchDto.TransportResult.builder()
                .id(301L)
                .providerName("Uber Corporate Fleet")
                .transportType("AIRPORT_TRANSFER")
                .pickupLocation(origin != null ? origin : "Indira Gandhi International Airport")
                .dropLocation(destination != null ? destination : "Taj Palace Diplomatic Enclave")
                .pickupTime(LocalDateTime.now().plusHours(3))
                .vehicleModel("Toyota Camry Hybrid / Executive Sedan")
                .price(BigDecimal.valueOf(850))
                .currencyCode("INR")
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .build());

        list.add(TravelSearchDto.TransportResult.builder()
                .id(302L)
                .providerName("Enterprise Rent-A-Car")
                .transportType("RENTAL_CAR")
                .pickupLocation("Airport Rental Terminal")
                .dropLocation("City Center Drop-off")
                .pickupTime(LocalDateTime.now().plusHours(3))
                .vehicleModel("Hyundai Creta SUV (Self-Drive)")
                .price(BigDecimal.valueOf(2400))
                .currencyCode("INR")
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .build());

        list.add(TravelSearchDto.TransportResult.builder()
                .id(303L)
                .providerName("Vande Bharat Express")
                .transportType("TRAIN")
                .pickupLocation("New Delhi Central Station")
                .dropLocation("Chandigarh Junction")
                .pickupTime(LocalDateTime.now().plusHours(5))
                .vehicleModel("Executive AC Chair Car")
                .price(BigDecimal.valueOf(1350))
                .currencyCode("INR")
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .build());

        return list;
    }

    @Override
    public boolean isLiveProvider() { return false; }
}
