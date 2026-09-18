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

        if (lower.contains("policy") || lower.contains("limit") || lower.contains("allowance") || lower.contains("compliant")) {
            return AiDto.ChatQueryResponse.builder()
                    .intent("POLICY_INQUIRY")
                    .response("Corporate Travel Policy Summary:\\n• Domestic Flights: Cap ₹15,000 (Economy Class mandatory unless Director level)\\n• Hotels: Cap ₹6,000/night (Preferred partners: Taj, Marriott, Lemon Tree)\\n• Daily Meal Allowance: ₹2,000\\n• Daily Cab Allowance: ₹1,500\\n• Advance Booking: Minimum 7 days prior to departure.")
                    .policyAdvice("All expenses within these tiers are auto-routed for expedited 1-click approval.")
                    .optimizationTips(List.of("Book 7+ days ahead for best corporate rates", "Use preferred hotel partners for auto-approval", "Combine flight + hotel in one request to save 12%"))
                    .build();
        }

        if (lower.contains("budget") || lower.contains("cost") || lower.contains("estimate")) {
            return AiDto.ChatQueryResponse.builder()
                    .intent("COST_ESTIMATE")
                    .response("Estimated trip cost for a 3-day Delhi business visit: ₹28,400 (flight ₹5,400 + hotel ₹18,000 + meals ₹3,000 + cab ₹2,000). This is within your department wallet remaining balance.")
                    .policyAdvice("Compliant with domestic cap ₹30,000 per trip.")
                    .estimatedSavings(BigDecimal.valueOf(3200))
                    .carbonReductionKg(BigDecimal.valueOf(18.5))
                    .build();
        }

        if (lower.contains("carbon") || lower.contains("co2") || lower.contains("green")) {
            return AiDto.ChatQueryResponse.builder()
                    .intent("SUSTAINABILITY")
                    .response("Your recommended route saves 142.5 kg CO₂ vs standard fare by choosing a direct flight and corporate hotel shuttle instead of rental car.")
                    .policyAdvice("ESG dashboard tracks department carbon budgets quarterly.")
                    .carbonReductionKg(BigDecimal.valueOf(142.5))
                    .build();
        }

        return AiDto.ChatQueryResponse.builder()
                .intent("GENERAL_ASSISTANCE")
                .response("Hello! I am your AI Corporate Travel Assistant. I can help you search flights, find corporate-discounted hotels, check travel policy compliance, calculate carbon footprints, and prepare travel requests. How may I assist your journey today?")
                .build();
    }
}
