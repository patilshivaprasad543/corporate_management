package com.corporate.travel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public final class FinanceReleaseDto {
    private FinanceReleaseDto() {}
    public record Request(@NotNull @DecimalMin("0.01") BigDecimal amount, @Size(max=500) String remarks) {}
    public record Release(Long id, Long travelRequestId, BigDecimal requestedAmount, BigDecimal releasedAmount,
                          String status, String paymentReference, String remarks) {}
}
