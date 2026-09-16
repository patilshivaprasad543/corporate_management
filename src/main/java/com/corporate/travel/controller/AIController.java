package com.corporate.travel.controller;

import com.corporate.travel.ai.AIExpenseReviewService;
import com.corporate.travel.ai.AITravelAssistantService;
import com.corporate.travel.ai.AITripOptimizerService;
import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AIExpenseReviewDto;
import com.corporate.travel.dto.AiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Travel & Expense", description = "AI-assisted travel planning, optimization and expense review")
public class AIController {
    private final AITravelAssistantService assistantService;
    private final AITripOptimizerService optimizerService;
    private final AIExpenseReviewService expenseReviewService;

    public AIController(AITravelAssistantService assistantService,
                        AITripOptimizerService optimizerService,
                        AIExpenseReviewService expenseReviewService) {
        this.assistantService = assistantService;
        this.optimizerService = optimizerService;
        this.expenseReviewService = expenseReviewService;
    }

    @PostMapping("/travel-assistant")
    @Operation(summary = "Chat with AI Corporate Travel Assistant")
    public ResponseEntity<ApiResponse<AiDto.ChatQueryResponse>> askAssistant(@RequestBody AiDto.ChatQueryRequest request) {
        String msg = request != null && request.getMessage() != null ? request.getMessage() : "Help me plan a compliant corporate trip";
        return ResponseEntity.ok(ApiResponse.ok(assistantService.processQuery(msg), "AI assistant response generated"));
    }

    @GetMapping("/trip-optimizer")
    @Operation(summary = "Get AI trip optimization proposal")
    public ResponseEntity<ApiResponse<AiDto.TripOptimizationProposal>> getOptimization() {
        return ResponseEntity.ok(ApiResponse.ok(optimizerService.generateOptimization(), "Trip optimization proposal generated"));
    }

    @PostMapping("/expense-review")
    @Operation(summary = "Review an expense for anomalies and Finance-review guidance")
    public ResponseEntity<ApiResponse<AIExpenseReviewDto.Response>> reviewExpense(@RequestBody AIExpenseReviewDto.Request request) {
        return ResponseEntity.ok(ApiResponse.ok(expenseReviewService.review(request), "AI expense review completed"));
    }
}
