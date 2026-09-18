package com.corporate.travel.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowDto {

    public static class HrBudgetSummary {
        private BigDecimal totalBudget;
        private BigDecimal utilized;
        private BigDecimal remaining;
        private Double utilizationPercentage;
        private List<AnalyticsDto.CategorySpend> utilizationBreakdown;
        private List<RecentTravelRequest> recentRequests;

        public HrBudgetSummary() {}

        public HrBudgetSummary(BigDecimal totalBudget, BigDecimal utilized, BigDecimal remaining,
                               Double utilizationPercentage,
                               List<AnalyticsDto.CategorySpend> utilizationBreakdown,
                               List<RecentTravelRequest> recentRequests) {
            this.totalBudget = totalBudget;
            this.utilized = utilized;
            this.remaining = remaining;
            this.utilizationPercentage = utilizationPercentage;
            this.utilizationBreakdown = utilizationBreakdown;
            this.recentRequests = recentRequests;
        }

        public BigDecimal getTotalBudget() { return totalBudget; }
        public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
        public BigDecimal getUtilized() { return utilized; }
        public void setUtilized(BigDecimal utilized) { this.utilized = utilized; }
        public BigDecimal getRemaining() { return remaining; }
        public void setRemaining(BigDecimal remaining) { this.remaining = remaining; }
        public Double getUtilizationPercentage() { return utilizationPercentage; }
        public void setUtilizationPercentage(Double utilizationPercentage) { this.utilizationPercentage = utilizationPercentage; }
        public List<AnalyticsDto.CategorySpend> getUtilizationBreakdown() { return utilizationBreakdown; }
        public void setUtilizationBreakdown(List<AnalyticsDto.CategorySpend> utilizationBreakdown) { this.utilizationBreakdown = utilizationBreakdown; }
        public List<RecentTravelRequest> getRecentRequests() { return recentRequests; }
        public void setRecentRequests(List<RecentTravelRequest> recentRequests) { this.recentRequests = recentRequests; }

        public static HrBudgetSummaryBuilder builder() { return new HrBudgetSummaryBuilder(); }

        public static class HrBudgetSummaryBuilder {
            private BigDecimal totalBudget;
            private BigDecimal utilized;
            private BigDecimal remaining;
            private Double utilizationPercentage;
            private List<AnalyticsDto.CategorySpend> utilizationBreakdown;
            private List<RecentTravelRequest> recentRequests;

            public HrBudgetSummaryBuilder totalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; return this; }
            public HrBudgetSummaryBuilder utilized(BigDecimal utilized) { this.utilized = utilized; return this; }
            public HrBudgetSummaryBuilder remaining(BigDecimal remaining) { this.remaining = remaining; return this; }
            public HrBudgetSummaryBuilder utilizationPercentage(Double utilizationPercentage) { this.utilizationPercentage = utilizationPercentage; return this; }
            public HrBudgetSummaryBuilder utilizationBreakdown(List<AnalyticsDto.CategorySpend> utilizationBreakdown) { this.utilizationBreakdown = utilizationBreakdown; return this; }
            public HrBudgetSummaryBuilder recentRequests(List<RecentTravelRequest> recentRequests) { this.recentRequests = recentRequests; return this; }

            public HrBudgetSummary build() {
                return new HrBudgetSummary(totalBudget, utilized, remaining, utilizationPercentage, utilizationBreakdown, recentRequests);
            }
        }
    }

    public static class RecentTravelRequest {
        private Long id;
        private String requestNumber;
        private String employeeName;
        private String route;
        private String status;
        private BigDecimal budget;

        public RecentTravelRequest() {}

        public RecentTravelRequest(Long id, String requestNumber, String employeeName, String route, String status, BigDecimal budget) {
            this.id = id;
            this.requestNumber = requestNumber;
            this.employeeName = employeeName;
            this.route = route;
            this.status = status;
            this.budget = budget;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getRequestNumber() { return requestNumber; }
        public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getRoute() { return route; }
        public void setRoute(String route) { this.route = route; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getBudget() { return budget; }
        public void setBudget(BigDecimal budget) { this.budget = budget; }
    }

    public static class PendingFundRelease {
        private Long id;
        private String reportNumber;
        private String employeeName;
        private String title;
        private BigDecimal amount;
        private LocalDateTime requestDate;
        private String status;

        public PendingFundRelease() {}

        public PendingFundRelease(Long id, String reportNumber, String employeeName, String title,
                                  BigDecimal amount, LocalDateTime requestDate, String status) {
            this.id = id;
            this.reportNumber = reportNumber;
            this.employeeName = employeeName;
            this.title = title;
            this.amount = amount;
            this.requestDate = requestDate;
            this.status = status;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReportNumber() { return reportNumber; }
        public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public LocalDateTime getRequestDate() { return requestDate; }
        public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class SupportDeskSummary {
        private Long activeBookings;
        private Long pendingServices;
        private Long supportTickets;
        private List<UpcomingItinerary> upcomingItineraries;

        public SupportDeskSummary() {}

        public SupportDeskSummary(Long activeBookings, Long pendingServices, Long supportTickets,
                                  List<UpcomingItinerary> upcomingItineraries) {
            this.activeBookings = activeBookings;
            this.pendingServices = pendingServices;
            this.supportTickets = supportTickets;
            this.upcomingItineraries = upcomingItineraries;
        }

        public Long getActiveBookings() { return activeBookings; }
        public void setActiveBookings(Long activeBookings) { this.activeBookings = activeBookings; }
        public Long getPendingServices() { return pendingServices; }
        public void setPendingServices(Long pendingServices) { this.pendingServices = pendingServices; }
        public Long getSupportTickets() { return supportTickets; }
        public void setSupportTickets(Long supportTickets) { this.supportTickets = supportTickets; }
        public List<UpcomingItinerary> getUpcomingItineraries() { return upcomingItineraries; }
        public void setUpcomingItineraries(List<UpcomingItinerary> upcomingItineraries) { this.upcomingItineraries = upcomingItineraries; }

        public static SupportDeskSummaryBuilder builder() { return new SupportDeskSummaryBuilder(); }

        public static class SupportDeskSummaryBuilder {
            private Long activeBookings;
            private Long pendingServices;
            private Long supportTickets;
            private List<UpcomingItinerary> upcomingItineraries;

            public SupportDeskSummaryBuilder activeBookings(Long activeBookings) { this.activeBookings = activeBookings; return this; }
            public SupportDeskSummaryBuilder pendingServices(Long pendingServices) { this.pendingServices = pendingServices; return this; }
            public SupportDeskSummaryBuilder supportTickets(Long supportTickets) { this.supportTickets = supportTickets; return this; }
            public SupportDeskSummaryBuilder upcomingItineraries(List<UpcomingItinerary> upcomingItineraries) { this.upcomingItineraries = upcomingItineraries; return this; }

            public SupportDeskSummary build() {
                return new SupportDeskSummary(activeBookings, pendingServices, supportTickets, upcomingItineraries);
            }
        }
    }

    public static class UpcomingItinerary {
        private Long bookingId;
        private String travelerName;
        private String route;
        private String departureDate;
        private String pnrNumber;
        private String status;

        public UpcomingItinerary() {}

        public UpcomingItinerary(Long bookingId, String travelerName, String route, String departureDate,
                                 String pnrNumber, String status) {
            this.bookingId = bookingId;
            this.travelerName = travelerName;
            this.route = route;
            this.departureDate = departureDate;
            this.pnrNumber = pnrNumber;
            this.status = status;
        }

        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
        public String getTravelerName() { return travelerName; }
        public void setTravelerName(String travelerName) { this.travelerName = travelerName; }
        public String getRoute() { return route; }
        public void setRoute(String route) { this.route = route; }
        public String getDepartureDate() { return departureDate; }
        public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
        public String getPnrNumber() { return pnrNumber; }
        public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
