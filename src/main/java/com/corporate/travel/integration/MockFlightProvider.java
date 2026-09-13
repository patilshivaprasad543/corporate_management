package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockFlightProvider implements FlightProvider {
    private static final Logger log = LoggerFactory.getLogger(MockFlightProvider.class);


    @Override
    public List<TravelSearchDto.FlightResult> searchFlights(TravelSearchDto.FlightSearchCriteria criteria) {
        List<TravelSearchDto.FlightResult> results = new ArrayList<>();
        String origin = (criteria.getOrigin() != null && !criteria.getOrigin().isBlank()) ? criteria.getOrigin() : "Hyderabad (HYD)";
        String dest = (criteria.getDestination() != null && !criteria.getDestination().isBlank()) ? criteria.getDestination() : "Delhi (DEL)";
        LocalDateTime baseTime = criteria.getDepartureDate() != null ? criteria.getDepartureDate().atTime(7, 30) : LocalDateTime.now().plusDays(5).withHour(7).withMinute(30);

        results.add(TravelSearchDto.FlightResult.builder()
                .id(101L)
                .flightNumber("AI-839")
                .airline("Air India")
                .airlineCode("AI")
                .originCity(origin)
                .originCode("HYD")
                .destinationCity(dest)
                .destinationCode("DEL")
                .departureTime(baseTime)
                .arrivalTime(baseTime.plusHours(2).plusMinutes(15))
                .durationMinutes(135)
                .stopsCount(0)
                .travelClass(criteria.getTravelClass() != null ? criteria.getTravelClass() : TravelClass.ECONOMY)
                .price(BigDecimal.valueOf(5400))
                .currencyCode("INR")
                .carbonEmissionsKg(BigDecimal.valueOf(142.5))
                .refundable(true)
                .baggageAllowance("25 kg Check-in, 7 kg Cabin")
                .corporatePreferred(true)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Fully Compliant with Tier-1 Domestic Travel Policy (Discount 12% applied)")
                .aiRecommended(true)
                .aiReason("Recommended: Lowest price among direct flights, arrives early morning, and preferred airline partner.")
                .build());

        results.add(TravelSearchDto.FlightResult.builder()
                .id(102L)
                .flightNumber("6E-512")
                .airline("IndiGo")
                .airlineCode("6E")
                .originCity(origin)
                .originCode("HYD")
                .destinationCity(dest)
                .destinationCode("DEL")
                .departureTime(baseTime.plusHours(3))
                .arrivalTime(baseTime.plusHours(5).plusMinutes(10))
                .durationMinutes(130)
                .stopsCount(0)
                .travelClass(criteria.getTravelClass() != null ? criteria.getTravelClass() : TravelClass.ECONOMY)
                .price(BigDecimal.valueOf(6100))
                .currencyCode("INR")
                .carbonEmissionsKg(BigDecimal.valueOf(138.0))
                .refundable(false)
                .baggageAllowance("15 kg Check-in, 7 kg Cabin")
                .corporatePreferred(true)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Compliant with corporate advance booking rules")
                .aiRecommended(false)
                .build());

        results.add(TravelSearchDto.FlightResult.builder()
                .id(103L)
                .flightNumber("UK-870")
                .airline("Vistara")
                .airlineCode("UK")
                .originCity(origin)
                .originCode("HYD")
                .destinationCity(dest)
                .destinationCode("DEL")
                .departureTime(baseTime.plusHours(6).plusMinutes(30))
                .arrivalTime(baseTime.plusHours(8).plusMinutes(45))
                .durationMinutes(135)
                .stopsCount(0)
                .travelClass(criteria.getTravelClass() != null ? criteria.getTravelClass() : TravelClass.ECONOMY)
                .price(BigDecimal.valueOf(8200))
                .currencyCode("INR")
                .carbonEmissionsKg(BigDecimal.valueOf(145.0))
                .refundable(true)
                .baggageAllowance("20 kg Check-in, 7 kg Cabin")
                .corporatePreferred(false)
                .complianceStatus(PolicyComplianceStatus.WARNING)
                .policyNote("Warning: Fare is ₹2,800 higher than best available corporate rate on this route")
                .aiRecommended(false)
                .build());

        results.add(TravelSearchDto.FlightResult.builder()
                .id(104L)
                .flightNumber("SG-102")
                .airline("SpiceJet")
                .airlineCode("SG")
                .originCity(origin)
                .originCode("HYD")
                .destinationCity(dest)
                .destinationCode("DEL")
                .departureTime(baseTime.plusHours(9))
                .arrivalTime(baseTime.plusHours(12).plusMinutes(30))
                .durationMinutes(210)
                .stopsCount(1)
                .travelClass(criteria.getTravelClass() != null ? criteria.getTravelClass() : TravelClass.ECONOMY)
                .price(BigDecimal.valueOf(4900))
                .currencyCode("INR")
                .carbonEmissionsKg(BigDecimal.valueOf(160.0))
                .refundable(false)
                .baggageAllowance("15 kg Check-in, 7 kg Cabin")
                .corporatePreferred(false)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Compliant: Budget-friendly 1-stop option")
                .aiRecommended(false)
                .build());

        return results;
    }

    @Override
    public boolean isLiveProvider() { return false; }

    @Override
    public String getProviderName() { return "Corporate Multi-GDS Mock Provider (Demo Mode)"; }
}
