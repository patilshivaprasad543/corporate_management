package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.ExpenseStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense_reports")

public class ExpenseReport extends BaseEntity {

    @Column(name = "report_number", nullable = false, unique = true, length = 50)
    private String reportNumber;

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id")
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private ExpenseStatus status = ExpenseStatus.DRAFT;

    @Column(name = "total_amount", precision = 12, scale = 2)

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "approved_amount", precision = 12, scale = 2)

    private BigDecimal approvedAmount = BigDecimal.ZERO;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Column(name = "has_ai_flags")

    private Boolean hasAiFlags = false;

    @Column(name = "ai_review_notes", length = 1000)
    private String aiReviewNotes;

    @OneToMany(mappedBy = "expenseReport", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<ExpenseItem> items = new ArrayList<>();


    public ExpenseReport() {}

    public ExpenseReport(String reportNumber, String title, TravelRequest travelRequest, User employee, Department department, CostCenter costCenter, ExpenseStatus status, BigDecimal totalAmount, BigDecimal approvedAmount, String currencyCode, Boolean hasAiFlags, String aiReviewNotes, List<ExpenseItem> items) {
        this.reportNumber = reportNumber;
        this.title = title;
        this.travelRequest = travelRequest;
        this.employee = employee;
        this.department = department;
        this.costCenter = costCenter;
        this.status = status;
        this.totalAmount = totalAmount;
        this.approvedAmount = approvedAmount;
        this.currencyCode = currencyCode;
        this.hasAiFlags = hasAiFlags;
        this.aiReviewNotes = aiReviewNotes;
        this.items = items;
    }

    public String getReportNumber() { return reportNumber; }

    public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public TravelRequest getTravelRequest() { return travelRequest; }

    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }

    public User getEmployee() { return employee; }

    public void setEmployee(User employee) { this.employee = employee; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public CostCenter getCostCenter() { return costCenter; }

    public void setCostCenter(CostCenter costCenter) { this.costCenter = costCenter; }

    public ExpenseStatus getStatus() { return status; }

    public void setStatus(ExpenseStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }

    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public Boolean isHasAiFlags() { return hasAiFlags; }

    public Boolean getHasAiFlags() { return hasAiFlags; }

    public void setHasAiFlags(Boolean hasAiFlags) { this.hasAiFlags = hasAiFlags; }

    public String getAiReviewNotes() { return aiReviewNotes; }

    public void setAiReviewNotes(String aiReviewNotes) { this.aiReviewNotes = aiReviewNotes; }

    public List<ExpenseItem> getItems() { return items; }

    public void setItems(List<ExpenseItem> items) { this.items = items; }

    public static ExpenseReportBuilder builder() { return new ExpenseReportBuilder(); }

    public static class ExpenseReportBuilder {
        private Long id;
        private String reportNumber;
        private String title;
        private TravelRequest travelRequest;
        private User employee;
        private Department department;
        private CostCenter costCenter;
        private ExpenseStatus status = ExpenseStatus.DRAFT;
        private BigDecimal totalAmount = BigDecimal.ZERO;
        private BigDecimal approvedAmount = BigDecimal.ZERO;
        private String currencyCode = "INR";
        private Boolean hasAiFlags = false;
        private String aiReviewNotes;
        private List<ExpenseItem> items = new ArrayList<>();

        public ExpenseReportBuilder id(Long id) { this.id = id; return this; }
        public ExpenseReportBuilder reportNumber(String reportNumber) { this.reportNumber = reportNumber; return this; }
        public ExpenseReportBuilder title(String title) { this.title = title; return this; }
        public ExpenseReportBuilder travelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; return this; }
        public ExpenseReportBuilder employee(User employee) { this.employee = employee; return this; }
        public ExpenseReportBuilder department(Department department) { this.department = department; return this; }
        public ExpenseReportBuilder costCenter(CostCenter costCenter) { this.costCenter = costCenter; return this; }
        public ExpenseReportBuilder status(ExpenseStatus status) { this.status = status; return this; }
        public ExpenseReportBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public ExpenseReportBuilder approvedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; return this; }
        public ExpenseReportBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public ExpenseReportBuilder hasAiFlags(Boolean hasAiFlags) { this.hasAiFlags = hasAiFlags; return this; }
        public ExpenseReportBuilder aiReviewNotes(String aiReviewNotes) { this.aiReviewNotes = aiReviewNotes; return this; }
        public ExpenseReportBuilder items(List<ExpenseItem> items) { this.items = items; return this; }

        public ExpenseReport build() {
            ExpenseReport obj = new ExpenseReport();
            obj.setId(this.id);
            obj.setReportNumber(this.reportNumber);
            obj.setTitle(this.title);
            obj.setTravelRequest(this.travelRequest);
            obj.setEmployee(this.employee);
            obj.setDepartment(this.department);
            obj.setCostCenter(this.costCenter);
            obj.setStatus(this.status);
            obj.setTotalAmount(this.totalAmount);
            obj.setApprovedAmount(this.approvedAmount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setHasAiFlags(this.hasAiFlags);
            obj.setAiReviewNotes(this.aiReviewNotes);
            obj.setItems(this.items);
            return obj;
        }
    }
}
