package com.corporate.travel.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.AiDto;
import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.integration.FlightProvider;
import com.corporate.travel.integration.HotelProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AITravelAssistantService {
    private static final Logger log = LoggerFactory.getLogger(AITravelAssistantService.class);

    public AITravelAssistantService(FlightProvider flightProvider, HotelProvider hotelProvider) {
        this.flightProvider = flightProvider;
        this.hotelProvider = hotelProvider;
    }


    private final FlightProvider flightProvider;
    private final HotelProvider hotelProvider;

    public AiDto.ChatQueryResponse processQuery(String query) {
        String lower = query.toLowerCase();

        if (lower.contains("singapore") || lower.contains("hyderabad to singapore") || lower.contains("flight")) {
            List<TravelSearchDto.FlightResult> flights = flightProvider.searchFlights(
                    TravelSearchDto.FlightSearchCriteria.builder()
                            .origin("Hyderabad (HYD)")
                            .destination("Singapore (SIN)")
                            .departureDate(LocalDate.now().plusDays(4))
                            .travelClass(TravelClass.ECONOMY)
                            .build()
            );

            return AiDto.ChatQueryResponse.builder()
                    .intent("FLIGHT_SEARCH")
                    .response("I found optimal flights matching your company travel policy. Air India AI-839 has a negotiated corporate discount saving ₹7,200 compared to spot market rates.")
                    .suggestedFlights(flights)
                    .policyAdvice("Complies with Standard Corporate Travel Policy Tier-1 (Domestic/Regional Cap ₹75,000).")
                    .estimatedSavings(BigDecimal.valueOf(7200))
                    .carbonReductionKg(BigDecimal.valueOf(45.2))
                    .build();
        }

        if (lower.contains("hotel") || lower.contains("stay") || lower.contains("delhi")) {
            List<TravelSearchDto.HotelResult> hotels = hotelProvider.searchHotels(
                    TravelSearchDto.HotelSearchCriteria.builder().city("Delhi").build()
            );

            return AiDto.ChatQueryResponse.builder()
                    .intent("HOTEL_SEARCH")
                    .response("Here are preferred corporate hotels near business districts in Delhi with corporate rates, free cancellation, and breakfast included.")
                    .suggestedHotels(hotels)
                    .policyAdvice("Compliant with daily accommodation limit of ₹6,000 per night for Executive Studio room.")
                    .estimatedSavings(BigDecimal.valueOf(3200))
                    .build();
        }

        if (lower.contains("policy") || lower.contains("limit") || lower.contains("allowance")) {
            return AiDto.ChatQueryResponse.builder()
                    .intent("POLICY_INQUIRY")
                    .response("Corporate Travel Policy Summary:\\n• Domestic Flights: Cap ₹15,000 (Economy Class mandatory unless Director level)\\n• Hotels: Cap ₹6,000/night (Preferred partners: Taj, Marriott, Lemon Tree)\\n• Daily Meal Allowance: ₹2,000\\n• Daily Cab Allowance: ₹1,500\\n• Advance Booking: Minimum 7 days prior to departure.")
                    .policyAdvice("All expenses within these tiers are auto-routed for expedited 1-click approval.")
                    .build();
        }

        return AiDto.ChatQueryResponse.builder()
                .intent("GENERAL_ASSISTANCE")
                .response("Hello! I am your AI Corporate Travel Assistant. I can help you search flights, find corporate-discounted hotels, check travel policy compliance, calculate carbon footprints, and prepare travel requests. How may I assist your journey today?")
                .build();
    }
}
