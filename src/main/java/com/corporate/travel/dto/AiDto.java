package com.corporate.travel.dto;

import java.math.BigDecimal;
import java.util.List;

public class AiDto {

    public static class ChatQueryRequest {
        private String query;
        private String message;
        private String context;

        public ChatQueryRequest() {}
        public ChatQueryRequest(String query, String context) {
            this.query = query;
            this.message = query;
            this.context = context;
        }

        public String getQuery() { return query != null ? query : message; }
        public void setQuery(String query) { this.query = query; if (this.message == null) this.message = query; }
        public String getMessage() { return message != null ? message : query; }
        public void setMessage(String message) { this.message = message; if (this.query == null) this.query = message; }
        public String getContext() { return context; }
        public void setContext(String context) { this.context = context; }

        public static ChatQueryRequestBuilder builder() { return new ChatQueryRequestBuilder(); }
        public static class ChatQueryRequestBuilder {
            private String query;
            private String message;
            private String context;
            public ChatQueryRequestBuilder query(String query) { this.query = query; this.message = query; return this; }
            public ChatQueryRequestBuilder message(String message) { this.message = message; this.query = message; return this; }
            public ChatQueryRequestBuilder context(String context) { this.context = context; return this; }
            public ChatQueryRequest build() { return new ChatQueryRequest(query != null ? query : message, context); }
        }
    }

    public static class ChatQueryResponse {
        private String intent;
        private String response;
        private List<TravelSearchDto.FlightResult> suggestedFlights;
        private List<TravelSearchDto.HotelResult> suggestedHotels;
        private String policyAdvice;
        private BigDecimal estimatedSavings;
        private BigDecimal carbonReductionKg;
        private List<String> optimizationTips;

        public ChatQueryResponse() {}

        public ChatQueryResponse(String intent, String response, List<TravelSearchDto.FlightResult> suggestedFlights, List<TravelSearchDto.HotelResult> suggestedHotels, String policyAdvice, BigDecimal estimatedSavings, BigDecimal carbonReductionKg, List<String> optimizationTips) {
            this.intent = intent;
            this.response = response;
            this.suggestedFlights = suggestedFlights;
            this.suggestedHotels = suggestedHotels;
            this.policyAdvice = policyAdvice;
            this.estimatedSavings = estimatedSavings;
            this.carbonReductionKg = carbonReductionKg;
            this.optimizationTips = optimizationTips;
        }

        public String getIntent() { return intent; }
        public void setIntent(String intent) { this.intent = intent; }
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
        public List<TravelSearchDto.FlightResult> getSuggestedFlights() { return suggestedFlights; }
        public void setSuggestedFlights(List<TravelSearchDto.FlightResult> suggestedFlights) { this.suggestedFlights = suggestedFlights; }
        public List<TravelSearchDto.HotelResult> getSuggestedHotels() { return suggestedHotels; }
        public void setSuggestedHotels(List<TravelSearchDto.HotelResult> suggestedHotels) { this.suggestedHotels = suggestedHotels; }
        public String getPolicyAdvice() { return policyAdvice; }
        public void setPolicyAdvice(String policyAdvice) { this.policyAdvice = policyAdvice; }
        public BigDecimal getEstimatedSavings() { return estimatedSavings; }
        public void setEstimatedSavings(BigDecimal estimatedSavings) { this.estimatedSavings = estimatedSavings; }
        public BigDecimal getCarbonReductionKg() { return carbonReductionKg; }
        public void setCarbonReductionKg(BigDecimal carbonReductionKg) { this.carbonReductionKg = carbonReductionKg; }
        public List<String> getOptimizationTips() { return optimizationTips; }
        public void setOptimizationTips(List<String> optimizationTips) { this.optimizationTips = optimizationTips; }

        public static ChatQueryResponseBuilder builder() { return new ChatQueryResponseBuilder(); }
        public static class ChatQueryResponseBuilder {
            private String intent;
            private String response;
            private List<TravelSearchDto.FlightResult> suggestedFlights;
            private List<TravelSearchDto.HotelResult> suggestedHotels;
            private String policyAdvice;
            private BigDecimal estimatedSavings;
            private BigDecimal carbonReductionKg;
            private List<String> optimizationTips;

            public ChatQueryResponseBuilder intent(String intent) { this.intent = intent; return this; }
            public ChatQueryResponseBuilder response(String response) { this.response = response; return this; }
            public ChatQueryResponseBuilder suggestedFlights(List<TravelSearchDto.FlightResult> suggestedFlights) { this.suggestedFlights = suggestedFlights; return this; }
            public ChatQueryResponseBuilder suggestedHotels(List<TravelSearchDto.HotelResult> suggestedHotels) { this.suggestedHotels = suggestedHotels; return this; }
            public ChatQueryResponseBuilder policyAdvice(String policyAdvice) { this.policyAdvice = policyAdvice; return this; }
            public ChatQueryResponseBuilder estimatedSavings(BigDecimal estimatedSavings) { this.estimatedSavings = estimatedSavings; return this; }
            public ChatQueryResponseBuilder carbonReductionKg(BigDecimal carbonReductionKg) { this.carbonReductionKg = carbonReductionKg; return this; }
            public ChatQueryResponseBuilder optimizationTips(List<String> optimizationTips) { this.optimizationTips = optimizationTips; return this; }

            public ChatQueryResponse build() {
                return new ChatQueryResponse(intent, response, suggestedFlights, suggestedHotels, policyAdvice, estimatedSavings, carbonReductionKg, optimizationTips);
            }
        }
    }

    public static class TripOptimizationProposal {
        private String originalOption;
        private String recommendedOption;
        private BigDecimal savingsAmount;
        private String savingsExplanation;
        private BigDecimal carbonSavedKg;
        private String scheduleImprovement;
        private Boolean withinPolicy;
        private String recommendation;
        private BigDecimal potentialSavings;
        private Double co2ReductionKg;
        private List<String> alternativeOptions;

        public TripOptimizationProposal() {}

        public String getOriginalOption() { return originalOption; }
        public void setOriginalOption(String originalOption) { this.originalOption = originalOption; }
        public String getRecommendedOption() { return recommendedOption; }
        public void setRecommendedOption(String recommendedOption) { this.recommendedOption = recommendedOption; }
        public BigDecimal getSavingsAmount() { return savingsAmount; }
        public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }
        public String getSavingsExplanation() { return savingsExplanation; }
        public void setSavingsExplanation(String savingsExplanation) { this.savingsExplanation = savingsExplanation; }
        public BigDecimal getCarbonSavedKg() { return carbonSavedKg; }
        public void setCarbonSavedKg(BigDecimal carbonSavedKg) { this.carbonSavedKg = carbonSavedKg; }
        public String getScheduleImprovement() { return scheduleImprovement; }
        public void setScheduleImprovement(String scheduleImprovement) { this.scheduleImprovement = scheduleImprovement; }
        public Boolean getWithinPolicy() { return withinPolicy; }
        public boolean isWithinPolicy() { return withinPolicy != null && withinPolicy; }
        public void setWithinPolicy(Boolean withinPolicy) { this.withinPolicy = withinPolicy; }
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
        public BigDecimal getPotentialSavings() { return potentialSavings; }
        public void setPotentialSavings(BigDecimal potentialSavings) { this.potentialSavings = potentialSavings; }
        public Double getCo2ReductionKg() { return co2ReductionKg; }
        public void setCo2ReductionKg(Double co2ReductionKg) { this.co2ReductionKg = co2ReductionKg; }
        public List<String> getAlternativeOptions() { return alternativeOptions; }
        public void setAlternativeOptions(List<String> alternativeOptions) { this.alternativeOptions = alternativeOptions; }

        public static TripOptimizationProposalBuilder builder() { return new TripOptimizationProposalBuilder(); }
        public static class TripOptimizationProposalBuilder {
            private String originalOption;
            private String recommendedOption;
            private BigDecimal savingsAmount;
            private String savingsExplanation;
            private BigDecimal carbonSavedKg;
            private String scheduleImprovement;
            private Boolean withinPolicy;
            private String recommendation;
            private BigDecimal potentialSavings;
            private Double co2ReductionKg;
            private List<String> alternativeOptions;

            public TripOptimizationProposalBuilder originalOption(String originalOption) { this.originalOption = originalOption; return this; }
            public TripOptimizationProposalBuilder recommendedOption(String recommendedOption) { this.recommendedOption = recommendedOption; return this; }
            public TripOptimizationProposalBuilder savingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; return this; }
            public TripOptimizationProposalBuilder savingsExplanation(String savingsExplanation) { this.savingsExplanation = savingsExplanation; return this; }
            public TripOptimizationProposalBuilder carbonSavedKg(BigDecimal carbonSavedKg) { this.carbonSavedKg = carbonSavedKg; return this; }
            public TripOptimizationProposalBuilder scheduleImprovement(String scheduleImprovement) { this.scheduleImprovement = scheduleImprovement; return this; }
            public TripOptimizationProposalBuilder withinPolicy(Boolean withinPolicy) { this.withinPolicy = withinPolicy; return this; }
            public TripOptimizationProposalBuilder recommendation(String recommendation) { this.recommendation = recommendation; return this; }
            public TripOptimizationProposalBuilder potentialSavings(BigDecimal potentialSavings) { this.potentialSavings = potentialSavings; return this; }
            public TripOptimizationProposalBuilder co2ReductionKg(Double co2ReductionKg) { this.co2ReductionKg = co2ReductionKg; return this; }
            public TripOptimizationProposalBuilder alternativeOptions(List<String> alternativeOptions) { this.alternativeOptions = alternativeOptions; return this; }

            public TripOptimizationProposal build() {
                TripOptimizationProposal t = new TripOptimizationProposal();
                t.originalOption = this.originalOption;
                t.recommendedOption = this.recommendedOption;
                t.savingsAmount = this.savingsAmount;
                t.savingsExplanation = this.savingsExplanation;
                t.carbonSavedKg = this.carbonSavedKg;
                t.scheduleImprovement = this.scheduleImprovement;
                t.withinPolicy = this.withinPolicy;
                t.recommendation = this.recommendation;
                t.potentialSavings = this.potentialSavings;
                t.co2ReductionKg = this.co2ReductionKg;
                t.alternativeOptions = this.alternativeOptions;
                return t;
            }
        }
    }
}
