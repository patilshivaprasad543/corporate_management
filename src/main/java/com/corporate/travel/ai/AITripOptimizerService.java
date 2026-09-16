package com.corporate.travel.ai;

import com.corporate.travel.dto.AiDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AITripOptimizerService {
    private final OpenAIService openAIService;

    public AITripOptimizerService(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public AiDto.TripOptimizationProposal generateOptimization() {
        if (openAIService.isEnabled()) {
            String advice = openAIService.generate(
                    "You are a corporate travel cost optimization analyst. Recommend practical ways to reduce total trip cost without violating company policy or creating unreasonable travel schedules. Do not invent prices, savings, availability, emissions or policy limits. Return a concise recommendation.",
                    "Review the current corporate trip and suggest optimization opportunities. The application will verify actual prices and policy compliance separately.");
            if (advice != null) {
                return AiDto.TripOptimizationProposal.builder()
                        .recommendation(advice)
                        .savingsExplanation("AI-generated recommendation; actual savings must be verified against provider results.")
                        .withinPolicy(null)
                        .alternativeOptions(List.of("Compare approved suppliers", "Check flexible cancellation options", "Consider advance booking"))
                        .build();
            }
        }

        return AiDto.TripOptimizationProposal.builder()
                .originalOption("Current selected travel option")
                .recommendedOption("Compare approved corporate alternatives")
                .savingsAmount(BigDecimal.ZERO)
                .savingsExplanation("Compare approved suppliers, cancellation terms and total trip cost before confirming.")
                .scheduleImprovement("Prefer options that meet the business schedule without unnecessary waiting time.")
                .withinPolicy(null)
                .recommendation("Review corporate supplier rates and policy before booking.")
                .alternativeOptions(List.of("Approved supplier rate", "Flexible fare", "Advance booking option"))
                .build();
    }
}
