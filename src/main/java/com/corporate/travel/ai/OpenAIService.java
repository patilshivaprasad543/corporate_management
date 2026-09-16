package com.corporate.travel.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Optional OpenAI integration. The application remains fully usable without an API key;
 * callers can fall back to deterministic corporate rules/provider data.
 */
@Service
public class OpenAIService {
    private final RestClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final boolean enabled;

    public OpenAIService(
            ObjectMapper objectMapper,
            @Value("${app.ai.openai.api-key:}") String apiKey,
            @Value("${app.ai.openai.model:gpt-5.6-luna}") String model,
            @Value("${app.ai.openai.enabled:false}") boolean enabled,
            @Value("${app.ai.openai.base-url:https://api.openai.com/v1}") String baseUrl) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.enabled = enabled && apiKey != null && !apiKey.isBlank();
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String generate(String instructions, String userInput) {
        if (!enabled) return null;

        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "instructions", instructions,
                    "input", userInput
            );

            String json = client.post()
                    .uri("/responses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            if (json == null || json.isBlank()) return null;
            JsonNode root = objectMapper.readTree(json);
            JsonNode outputText = root.get("output_text");
            if (outputText != null && outputText.isTextual()) return outputText.asText();

            JsonNode output = root.get("output");
            if (output != null && output.isArray()) {
                for (JsonNode item : output) {
                    JsonNode content = item.get("content");
                    if (content != null && content.isArray()) {
                        for (JsonNode part : content) {
                            JsonNode text = part.get("text");
                            if (text != null && text.isTextual()) return text.asText();
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // AI is an enhancement, never a reason for core travel operations to fail.
        }
        return null;
    }
}
