package com.corporate.travel.controller;

import com.corporate.travel.ai.AITravelAssistantService;
import com.corporate.travel.ai.AITripOptimizerService;
import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.AiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Travel Assistant", description = "Natural Language Corporate Travel Assistant and Smart Trip Optimization Engine")
public class AIController {
    public AIController(AITravelAssistantService assistantService, AITripOptimizerService optimizerService) {
        this.assistantService = assistantService;
        this.optimizerService = optimizerService;
    }


    private final AITravelAssistantService assistantService;
    private final AITripOptimizerService optimizerService;

    @PostMapping("/travel-assistant")
    @Operation(summary = "Chat with AI Corporate Travel Assistant")
    public ResponseEntity<ApiResponse<AiDto.ChatQueryResponse>> askAssistant(@RequestBody AiDto.ChatQueryRequest request) {
        String msg = request != null && request.getMessage() != null ? request.getMessage() : "Find me flights to Singapore";
        return ResponseEntity.ok(ApiResponse.ok(assistantService.processQuery(msg), "AI assistant response generated"));
    }

    @GetMapping("/trip-optimizer")
    @Operation(summary = "Get AI trip optimization and savings proposal")
    public ResponseEntity<ApiResponse<AiDto.TripOptimizationProposal>> getOptimization() {
        return ResponseEntity.ok(ApiResponse.ok(optimizerService.generateOptimization(), "Trip optimization proposal generated"));
    }

}
