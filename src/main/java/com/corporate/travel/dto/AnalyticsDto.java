package com.corporate.travel.dto;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AnalyticsDto {

    public static class ExecutiveDashboardSummary {
        private BigDecimal totalTravelSpend;
        private BigDecimal totalSavings;
        private Long totalTrips;
        private BigDecimal averageTripCost;
        private Double policyComplianceRate;
        private BigDecimal totalCarbonEmissionsKg;
        private Long activeTravelersCount;
        private Long pendingApprovalsCount;
        private List<MonthlySpend> monthlyTrends;
        private List<DepartmentSpend> departmentBreakdown;
        private List<TopDestination> topDestinations;
        private List<CategorySpend> categoryBreakdown;

    public ExecutiveDashboardSummary() {}

    public ExecutiveDashboardSummary(BigDecimal totalTravelSpend, BigDecimal totalSavings, Long totalTrips, BigDecimal averageTripCost, Double policyComplianceRate, BigDecimal totalCarbonEmissionsKg, Long activeTravelersCount, Long pendingApprovalsCount, List<MonthlySpend> monthlyTrends, List<DepartmentSpend> departmentBreakdown, List<TopDestination> topDestinations, List<CategorySpend> categoryBreakdown) {
        this.totalTravelSpend = totalTravelSpend;
        this.totalSavings = totalSavings;
        this.totalTrips = totalTrips;
        this.averageTripCost = averageTripCost;
        this.policyComplianceRate = policyComplianceRate;
        this.totalCarbonEmissionsKg = totalCarbonEmissionsKg;
        this.activeTravelersCount = activeTravelersCount;
        this.pendingApprovalsCount = pendingApprovalsCount;
        this.monthlyTrends = monthlyTrends;
        this.departmentBreakdown = departmentBreakdown;
        this.topDestinations = topDestinations;
        this.categoryBreakdown = categoryBreakdown;
    }

    public BigDecimal getTotalTravelSpend() { return totalTravelSpend; }

    public void setTotalTravelSpend(BigDecimal totalTravelSpend) { this.totalTravelSpend = totalTravelSpend; }

    public BigDecimal getTotalSavings() { return totalSavings; }

    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }

    public Long getTotalTrips() { return totalTrips; }

    public void setTotalTrips(Long totalTrips) { this.totalTrips = totalTrips; }

    public BigDecimal getAverageTripCost() { return averageTripCost; }

    public void setAverageTripCost(BigDecimal averageTripCost) { this.averageTripCost = averageTripCost; }

    public Double getPolicyComplianceRate() { return policyComplianceRate; }

    public void setPolicyComplianceRate(Double policyComplianceRate) { this.policyComplianceRate = policyComplianceRate; }

    public BigDecimal getTotalCarbonEmissionsKg() { return totalCarbonEmissionsKg; }

    public void setTotalCarbonEmissionsKg(BigDecimal totalCarbonEmissionsKg) { this.totalCarbonEmissionsKg = totalCarbonEmissionsKg; }

    public Long getActiveTravelersCount() { return activeTravelersCount; }

    public void setActiveTravelersCount(Long activeTravelersCount) { this.activeTravelersCount = activeTravelersCount; }

    public Long getPendingApprovalsCount() { return pendingApprovalsCount; }

    public void setPendingApprovalsCount(Long pendingApprovalsCount) { this.pendingApprovalsCount = pendingApprovalsCount; }

    public List<MonthlySpend> getMonthlyTrends() { return monthlyTrends; }

    public void setMonthlyTrends(List<MonthlySpend> monthlyTrends) { this.monthlyTrends = monthlyTrends; }

    public List<DepartmentSpend> getDepartmentBreakdown() { return departmentBreakdown; }

    public void setDepartmentBreakdown(List<DepartmentSpend> departmentBreakdown) { this.departmentBreakdown = departmentBreakdown; }

    public List<TopDestination> getTopDestinations() { return topDestinations; }

    public void setTopDestinations(List<TopDestination> topDestinations) { this.topDestinations = topDestinations; }

    public List<CategorySpend> getCategoryBreakdown() { return categoryBreakdown; }

    public void setCategoryBreakdown(List<CategorySpend> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }

    public static ExecutiveDashboardSummaryBuilder builder() { return new ExecutiveDashboardSummaryBuilder(); }

    public static class ExecutiveDashboardSummaryBuilder {
        private BigDecimal totalTravelSpend;
        private BigDecimal totalSavings;
        private Long totalTrips;
        private BigDecimal averageTripCost;
        private Double policyComplianceRate;
        private BigDecimal totalCarbonEmissionsKg;
        private Long activeTravelersCount;
        private Long pendingApprovalsCount;
        private List<MonthlySpend> monthlyTrends;
        private List<DepartmentSpend> departmentBreakdown;
        private List<TopDestination> topDestinations;
        private List<CategorySpend> categoryBreakdown;

        public ExecutiveDashboardSummaryBuilder totalTravelSpend(BigDecimal totalTravelSpend) { this.totalTravelSpend = totalTravelSpend; return this; }
        public ExecutiveDashboardSummaryBuilder totalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; return this; }
        public ExecutiveDashboardSummaryBuilder totalTrips(Long totalTrips) { this.totalTrips = totalTrips; return this; }
        public ExecutiveDashboardSummaryBuilder averageTripCost(BigDecimal averageTripCost) { this.averageTripCost = averageTripCost; return this; }
        public ExecutiveDashboardSummaryBuilder policyComplianceRate(Double policyComplianceRate) { this.policyComplianceRate = policyComplianceRate; return this; }
        public ExecutiveDashboardSummaryBuilder totalCarbonEmissionsKg(BigDecimal totalCarbonEmissionsKg) { this.totalCarbonEmissionsKg = totalCarbonEmissionsKg; return this; }
        public ExecutiveDashboardSummaryBuilder activeTravelersCount(Long activeTravelersCount) { this.activeTravelersCount = activeTravelersCount; return this; }
        public ExecutiveDashboardSummaryBuilder pendingApprovalsCount(Long pendingApprovalsCount) { this.pendingApprovalsCount = pendingApprovalsCount; return this; }
        public ExecutiveDashboardSummaryBuilder monthlyTrends(List<MonthlySpend> monthlyTrends) { this.monthlyTrends = monthlyTrends; return this; }
        public ExecutiveDashboardSummaryBuilder departmentBreakdown(List<DepartmentSpend> departmentBreakdown) { this.departmentBreakdown = departmentBreakdown; return this; }
        public ExecutiveDashboardSummaryBuilder topDestinations(List<TopDestination> topDestinations) { this.topDestinations = topDestinations; return this; }
        public ExecutiveDashboardSummaryBuilder categoryBreakdown(List<CategorySpend> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; return this; }

        public ExecutiveDashboardSummary build() {
            ExecutiveDashboardSummary obj = new ExecutiveDashboardSummary();
            obj.setTotalTravelSpend(this.totalTravelSpend);
            obj.setTotalSavings(this.totalSavings);
            obj.setTotalTrips(this.totalTrips);
            obj.setAverageTripCost(this.averageTripCost);
            obj.setPolicyComplianceRate(this.policyComplianceRate);
            obj.setTotalCarbonEmissionsKg(this.totalCarbonEmissionsKg);
            obj.setActiveTravelersCount(this.activeTravelersCount);
            obj.setPendingApprovalsCount(this.pendingApprovalsCount);
            obj.setMonthlyTrends(this.monthlyTrends);
            obj.setDepartmentBreakdown(this.departmentBreakdown);
            obj.setTopDestinations(this.topDestinations);
            obj.setCategoryBreakdown(this.categoryBreakdown);
            return obj;
        }
    }
    }

    public static class MonthlySpend {
        private String month;
        private BigDecimal flightSpend;
        private BigDecimal hotelSpend;
        private BigDecimal transportSpend;
        private BigDecimal totalSpend;

    public MonthlySpend() {}

    public MonthlySpend(String month, BigDecimal flightSpend, BigDecimal hotelSpend, BigDecimal transportSpend, BigDecimal totalSpend) {
        this.month = month;
        this.flightSpend = flightSpend;
        this.hotelSpend = hotelSpend;
        this.transportSpend = transportSpend;
        this.totalSpend = totalSpend;
    }

    public String getMonth() { return month; }

    public void setMonth(String month) { this.month = month; }

    public BigDecimal getFlightSpend() { return flightSpend; }

    public void setFlightSpend(BigDecimal flightSpend) { this.flightSpend = flightSpend; }

    public BigDecimal getHotelSpend() { return hotelSpend; }

    public void setHotelSpend(BigDecimal hotelSpend) { this.hotelSpend = hotelSpend; }

    public BigDecimal getTransportSpend() { return transportSpend; }

    public void setTransportSpend(BigDecimal transportSpend) { this.transportSpend = transportSpend; }

    public BigDecimal getTotalSpend() { return totalSpend; }

    public void setTotalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; }

    public static MonthlySpendBuilder builder() { return new MonthlySpendBuilder(); }

    public static class MonthlySpendBuilder {
        private String month;
        private BigDecimal flightSpend;
        private BigDecimal hotelSpend;
        private BigDecimal transportSpend;
        private BigDecimal totalSpend;

        public MonthlySpendBuilder month(String month) { this.month = month; return this; }
        public MonthlySpendBuilder flightSpend(BigDecimal flightSpend) { this.flightSpend = flightSpend; return this; }
        public MonthlySpendBuilder hotelSpend(BigDecimal hotelSpend) { this.hotelSpend = hotelSpend; return this; }
        public MonthlySpendBuilder transportSpend(BigDecimal transportSpend) { this.transportSpend = transportSpend; return this; }
        public MonthlySpendBuilder totalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; return this; }

        public MonthlySpend build() {
            MonthlySpend obj = new MonthlySpend();
            obj.setMonth(this.month);
            obj.setFlightSpend(this.flightSpend);
            obj.setHotelSpend(this.hotelSpend);
            obj.setTransportSpend(this.transportSpend);
            obj.setTotalSpend(this.totalSpend);
            return obj;
        }
    }
    }

    public static class DepartmentSpend {
        private String departmentName;
        private BigDecimal allocatedBudget;
        private BigDecimal spentAmount;
        private BigDecimal remainingBudget;
        private Double budgetUsedPercentage;

    public DepartmentSpend() {}

    public DepartmentSpend(String departmentName, BigDecimal allocatedBudget, BigDecimal spentAmount, BigDecimal remainingBudget, Double budgetUsedPercentage) {
        this.departmentName = departmentName;
        this.allocatedBudget = allocatedBudget;
        this.spentAmount = spentAmount;
        this.remainingBudget = remainingBudget;
        this.budgetUsedPercentage = budgetUsedPercentage;
    }

    public String getDepartmentName() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public BigDecimal getAllocatedBudget() { return allocatedBudget; }

    public void setAllocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; }

    public BigDecimal getSpentAmount() { return spentAmount; }

    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }

    public BigDecimal getRemainingBudget() { return remainingBudget; }

    public void setRemainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; }

    public Double getBudgetUsedPercentage() { return budgetUsedPercentage; }

    public void setBudgetUsedPercentage(Double budgetUsedPercentage) { this.budgetUsedPercentage = budgetUsedPercentage; }

    public static DepartmentSpendBuilder builder() { return new DepartmentSpendBuilder(); }

    public static class DepartmentSpendBuilder {
        private String departmentName;
        private BigDecimal allocatedBudget;
        private BigDecimal spentAmount;
        private BigDecimal remainingBudget;
        private Double budgetUsedPercentage;

        public DepartmentSpendBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public DepartmentSpendBuilder allocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; return this; }
        public DepartmentSpendBuilder spentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; return this; }
        public DepartmentSpendBuilder remainingBudget(BigDecimal remainingBudget) { this.remainingBudget = remainingBudget; return this; }
        public DepartmentSpendBuilder budgetUsedPercentage(Double budgetUsedPercentage) { this.budgetUsedPercentage = budgetUsedPercentage; return this; }

        public DepartmentSpend build() {
            DepartmentSpend obj = new DepartmentSpend();
            obj.setDepartmentName(this.departmentName);
            obj.setAllocatedBudget(this.allocatedBudget);
            obj.setSpentAmount(this.spentAmount);
            obj.setRemainingBudget(this.remainingBudget);
            obj.setBudgetUsedPercentage(this.budgetUsedPercentage);
            return obj;
        }
    }
    }

    public static class TopDestination {
        private String destination;
        private Long tripCount;
        private BigDecimal totalSpend;

    public TopDestination() {}

    public TopDestination(String destination, Long tripCount, BigDecimal totalSpend) {
        this.destination = destination;
        this.tripCount = tripCount;
        this.totalSpend = totalSpend;
    }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public Long getTripCount() { return tripCount; }

    public void setTripCount(Long tripCount) { this.tripCount = tripCount; }

    public BigDecimal getTotalSpend() { return totalSpend; }

    public void setTotalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; }

    public static TopDestinationBuilder builder() { return new TopDestinationBuilder(); }

    public static class TopDestinationBuilder {
        private String destination;
        private Long tripCount;
        private BigDecimal totalSpend;

        public TopDestinationBuilder destination(String destination) { this.destination = destination; return this; }
        public TopDestinationBuilder tripCount(Long tripCount) { this.tripCount = tripCount; return this; }
        public TopDestinationBuilder totalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; return this; }

        public TopDestination build() {
            TopDestination obj = new TopDestination();
            obj.setDestination(this.destination);
            obj.setTripCount(this.tripCount);
            obj.setTotalSpend(this.totalSpend);
            return obj;
        }
    }
    }

    public static class CategorySpend {
        private String category;
        private BigDecimal amount;
        private Double percentage;

    public CategorySpend() {}

    public CategorySpend(String category, BigDecimal amount, Double percentage) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Double getPercentage() { return percentage; }

    public void setPercentage(Double percentage) { this.percentage = percentage; }

    public static CategorySpendBuilder builder() { return new CategorySpendBuilder(); }

    public static class CategorySpendBuilder {
        private String category;
        private BigDecimal amount;
        private Double percentage;

        public CategorySpendBuilder category(String category) { this.category = category; return this; }
        public CategorySpendBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public CategorySpendBuilder percentage(Double percentage) { this.percentage = percentage; return this; }

        public CategorySpend build() {
            CategorySpend obj = new CategorySpend();
            obj.setCategory(this.category);
            obj.setAmount(this.amount);
            obj.setPercentage(this.percentage);
            return obj;
        }
    }
    }


    public AnalyticsDto() {}

    public static AnalyticsDtoBuilder builder() { return new AnalyticsDtoBuilder(); }

    public static class AnalyticsDtoBuilder {


        public AnalyticsDto build() {
            AnalyticsDto obj = new AnalyticsDto();
            return obj;
        }
    }
}
