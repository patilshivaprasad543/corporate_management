package com.corporate.travel.ai;

import com.corporate.travel.dto.AiDto;
import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.integration.FlightProvider;
import com.corporate.travel.integration.HotelProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AITravelAssistantService {
    private final FlightProvider flightProvider;
    private final HotelProvider hotelProvider;
    private final OpenAIService openAIService;

    public AITravelAssistantService(FlightProvider flightProvider, HotelProvider hotelProvider, OpenAIService openAIService) {
        this.flightProvider = flightProvider;
        this.hotelProvider = hotelProvider;
        this.openAIService = openAIService;
    }

    public AiDto.ChatQueryResponse processQuery(String query) {
        String safeQuery = query == null || query.isBlank() ? "Help me plan a compliant corporate trip" : query.trim();

        // AI is used for natural-language interpretation/advice, while booking and policy data
        // continue to come from trusted application services.
        if (openAIService.isEnabled()) {
            String aiResponse = openAIService.generate(
                    "You are the corporate travel assistant. Give concise, practical travel guidance. " +
                    "Never invent flight prices, availability, policy limits, approvals, or bookings. " +
                    "If data is not supplied, say that it must be checked in the company's travel system. " +
                    "Respect corporate travel policy and recommend the lowest reasonable total cost while considering schedule and traveler safety.",
                    safeQuery);
            if (aiResponse != null) {
                return AiDto.ChatQueryResponse.builder()
                        .intent("AI_TRAVEL_ASSISTANCE")
                        .response(aiResponse)
                        .optimizationTips(List.of(
                                "Use the Search Travel screen to verify live provider availability.",
                                "Submit a Travel Request before making a non-refundable booking.",
                                "Keep receipts and corporate-card references for expense reconciliation."
                        ))
                        .build();
            }
        }

        return ruleBasedResponse(safeQuery);
    }

    private AiDto.ChatQueryResponse ruleBasedResponse(String query) {
        String lower = query.toLowerCase();
        if (lower.contains("singapore") || lower.contains("flight")) {
            List<TravelSearchDto.FlightResult> flights = flightProvider.searchFlights(
                    TravelSearchDto.FlightSearchCriteria.builder()
                            .origin("Hyderabad (HYD)")
                            .destination("Singapore (SIN)")
                            .departureDate(LocalDate.now().plusDays(4))
                            .travelClass(TravelClass.ECONOMY)
                            .build());
            return AiDto.ChatQueryResponse.builder()
                    .intent("FLIGHT_SEARCH")
                    .response("I found flight options from the configured provider. Review policy compliance and availability before booking.")
                    .suggestedFlights(flights)
                    .policyAdvice("Final compliance must be checked against the employee's active corporate travel policy.")
                    .estimatedSavings(BigDecimal.ZERO)
                    .build();
        }
        if (lower.contains("hotel") || lower.contains("stay")) {
            List<TravelSearchDto.HotelResult> hotels = hotelProvider.searchHotels(
                    TravelSearchDto.HotelSearchCriteria.builder().city("Delhi").build());
            return AiDto.ChatQueryResponse.builder()
                    .intent("HOTEL_SEARCH")
                    .response("Here are hotel options from the configured provider. Compare corporate rates, cancellation terms and policy limits before booking.")
                    .suggestedHotels(hotels)
                    .build();
        }
        if (lower.contains("policy") || lower.contains("limit") || lower.contains("allowance")) {
            return AiDto.ChatQueryResponse.builder()
                    .intent("POLICY_INQUIRY")
                    .response("I can explain the active corporate travel policy, but the authoritative limits should come from the policy configured for your organization and employee grade.")
                    .policyAdvice("Check Travel Policies before submitting or booking a trip.")
                    .build();
        }
        return AiDto.ChatQueryResponse.builder()
                .intent("GENERAL_ASSISTANCE")
                .response("I can help interpret a corporate travel request, explain policy requirements, compare trip options, and suggest ways to reduce travel cost and administrative work.")
                .build();
    }
}
