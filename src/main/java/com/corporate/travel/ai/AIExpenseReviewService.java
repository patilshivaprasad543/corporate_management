package com.corporate.travel.ai;

import com.corporate.travel.dto.AIExpenseReviewDto;
import com.corporate.travel.entity.enums.ExpenseCategory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIExpenseReviewService {
    private final OpenAIService openAIService;

    public AIExpenseReviewService(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public AIExpenseReviewDto.Response review(AIExpenseReviewDto.Request request) {
        List<String> flags = new ArrayList<>();
        BigDecimal amount = request.getAmount();
        if (amount == null || amount.signum() < 0) {
            flags.add("Expense amount must be a non-negative value");
        }
        if (amount != null && amount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            flags.add("High-value expense should receive manual Finance review");
        }
        if (request.getCategory() == ExpenseCategory.MEALS && amount != null && amount.compareTo(BigDecimal.valueOf(2000)) > 0) {
            flags.add("Meal expense is above the configured ₹2,000 policy reference limit");
        }
        if (request.getMerchantName() == null || request.getMerchantName().isBlank()) {
            flags.add("Merchant name is missing");
        }

        String recommendation = flags.isEmpty()
                ? "No obvious anomaly was detected. Finance should still verify the receipt and active company policy."
                : "Route this expense for human Finance review before reimbursement.";
        boolean aiGenerated = false;

        if (openAIService.isEnabled()) {
            String prompt = "Category=" + request.getCategory() +
                    ", amount=" + amount +
                    ", tax=" + request.getTaxAmount() +
                    ", merchant=" + request.getMerchantName() +
                    ", description=" + request.getDescription();
            String aiAdvice = openAIService.generate(
                    "You are an enterprise expense-review assistant. Identify possible anomalies or missing information. Do not declare fraud, do not invent policy limits, and never approve or reject reimbursement. Return concise advice for a human Finance reviewer.",
                    prompt);
            if (aiAdvice != null && !aiAdvice.isBlank()) {
                recommendation = aiAdvice;
                aiGenerated = true;
            }
        }
        return new AIExpenseReviewDto.Response(!flags.isEmpty(), flags, recommendation, aiGenerated);
    }
}
