package com.corporate.travel.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.AiDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AITripOptimizerService {
    private static final Logger log = LoggerFactory.getLogger(AITripOptimizerService.class);


    public AiDto.TripOptimizationProposal generateOptimization() {
        return AiDto.TripOptimizationProposal.builder()
                .originalOption("Non-preferred Airline (11:30 AM Departure, ₹12,600)")
                .recommendedOption("Air India Corporate Partner (07:30 AM Departure, ₹5,400)")
                .savingsAmount(BigDecimal.valueOf(7200))
                .savingsExplanation("AI Recommendation: Saves ₹7,200, arrives in time for morning business meetings, and provides zero cancellation fee.")
                .carbonSavedKg(BigDecimal.valueOf(18.5))
                .scheduleImprovement("Arrives 4 hours earlier, avoiding peak traffic transfer.")
                .withinPolicy(true)
                .build();
    }
}
