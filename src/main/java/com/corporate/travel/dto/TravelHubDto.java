package com.corporate.travel.dto;

import com.corporate.travel.entity.TravelBudget;
import com.corporate.travel.entity.TravelDocument;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public final class TravelHubDto {
    private TravelHubDto() {}

    public record BudgetRequest(
        @NotNull @DecimalMin("0.00") BigDecimal allocatedAmount,
        @DecimalMin("0.00") BigDecimal flightLimit,
        @DecimalMin("0.00") BigDecimal hotelLimit,
        @DecimalMin("0.00") BigDecimal cabLimit,
        @DecimalMin("0.00") BigDecimal mealLimit,
        @DecimalMin("0.00") BigDecimal otherLimit
    ) {}

    public record BudgetResponse(Long travelRequestId, BigDecimal allocatedAmount, BigDecimal flightLimit,
                                 BigDecimal hotelLimit, BigDecimal cabLimit, BigDecimal mealLimit,
                                 BigDecimal otherLimit, TravelBudget.BudgetStatus status) {}

    public record AgentContactRequest(@NotNull @Size(max=200) String agencyName,
                                      @NotNull @Size(max=150) String agentName,
                                      @NotNull @Size(max=40) String phoneNumber,
                                      @Size(max=200) String email,
                                      @Size(max=40) String emergencyPhone,
                                      @Size(max=500) String notes) {}

    public record AgentContactResponse(Long travelRequestId, String agencyName, String agentName,
                                       String phoneNumber, String email, String emergencyPhone, String notes) {}

    public record DocumentResponse(Long id, Long bookingId, TravelDocument.DocumentType documentType,
                                   String fileName, String description, String downloadUrl) {}

    public record ChatRequest(@NotNull @Size(min=1, max=4000) String message,
                              @Size(max=255) String attachmentName,
                              @Size(max=500) String attachmentKey) {}

    public record ChatMessageResponse(Long id, Long senderUserId, String senderName,
                                      String senderRole, String message, String attachmentName,
                                      Instant createdAt) {}

    public record TravelHubResponse(Long travelRequestId, String requestNumber, String tripName,
                                    String status, BudgetResponse budget, AgentContactResponse agent,
                                    List<DocumentResponse> documents, List<ChatMessageResponse> chat) {}
}
