package com.corporate.travel;

import com.corporate.travel.ai.AIExpenseReviewService;
import com.corporate.travel.ai.OpenAIService;
import com.corporate.travel.dto.AIExpenseReviewDto;
import com.corporate.travel.entity.enums.ExpenseCategory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AIExpenseReviewServiceTest {
    @Test
    void flagsHighValueAndOverLimitMeals() {
        OpenAIService openAI = Mockito.mock(OpenAIService.class);
        Mockito.when(openAI.isEnabled()).thenReturn(false);
        AIExpenseReviewService service = new AIExpenseReviewService(openAI);

        AIExpenseReviewDto.Request request = new AIExpenseReviewDto.Request();
        request.setCategory(ExpenseCategory.MEALS);
        request.setAmount(new BigDecimal("55000"));
        request.setMerchantName("Business Restaurant");
        request.setDescription("Client dinner");

        AIExpenseReviewDto.Response response = service.review(request);
        assertTrue(response.isFlagged());
        assertEquals(2, response.getFlags().size());
        assertFalse(response.isAiGenerated());
    }

    @Test
    void cleanExpenseGetsHumanVerificationRecommendation() {
        OpenAIService openAI = Mockito.mock(OpenAIService.class);
        Mockito.when(openAI.isEnabled()).thenReturn(false);
        AIExpenseReviewService service = new AIExpenseReviewService(openAI);

        AIExpenseReviewDto.Request request = new AIExpenseReviewDto.Request();
        request.setCategory(ExpenseCategory.TAXI);
        request.setAmount(new BigDecimal("850"));
        request.setMerchantName("City Cab");

        AIExpenseReviewDto.Response response = service.review(request);
        assertFalse(response.isFlagged());
        assertFalse(response.isAiGenerated());
        assertTrue(response.getRecommendation().contains("Finance"));
    }
}
