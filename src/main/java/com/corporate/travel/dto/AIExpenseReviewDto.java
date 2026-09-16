package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.ExpenseCategory;
import java.math.BigDecimal;
import java.util.List;

public class AIExpenseReviewDto {
    public static class Request {
        private ExpenseCategory category;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String merchantName;
        private String description;
        private String currencyCode;

        public Request() {}
        public ExpenseCategory getCategory() { return category; }
        public void setCategory(ExpenseCategory category) { this.category = category; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public BigDecimal getTaxAmount() { return taxAmount; }
        public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
        public String getMerchantName() { return merchantName; }
        public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    }

    public static class Response {
        private boolean flagged;
        private List<String> flags;
        private String recommendation;
        private boolean aiGenerated;

        public Response(boolean flagged, List<String> flags, String recommendation, boolean aiGenerated) {
            this.flagged = flagged;
            this.flags = flags;
            this.recommendation = recommendation;
            this.aiGenerated = aiGenerated;
        }
        public boolean isFlagged() { return flagged; }
        public List<String> getFlags() { return flags; }
        public String getRecommendation() { return recommendation; }
        public boolean isAiGenerated() { return aiGenerated; }
    }
}
