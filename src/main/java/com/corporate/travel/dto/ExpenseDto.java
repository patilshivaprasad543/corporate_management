package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.ExpenseCategory;
import com.corporate.travel.entity.enums.ExpenseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseDto {

    public static class CreateReportRequest {
        @NotBlank
        private String title;
        private Long travelRequestId;
        private Long costCenterId;
        private List<CreateItemRequest> items;

    public CreateReportRequest() {}

    public CreateReportRequest(String title, Long travelRequestId, Long costCenterId, List<CreateItemRequest> items) {
        this.title = title;
        this.travelRequestId = travelRequestId;
        this.costCenterId = costCenterId;
        this.items = items;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Long getTravelRequestId() { return travelRequestId; }

    public void setTravelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; }

    public Long getCostCenterId() { return costCenterId; }

    public void setCostCenterId(Long costCenterId) { this.costCenterId = costCenterId; }

    public List<CreateItemRequest> getItems() { return items; }

    public void setItems(List<CreateItemRequest> items) { this.items = items; }

    public static CreateReportRequestBuilder builder() { return new CreateReportRequestBuilder(); }

    public static class CreateReportRequestBuilder {
        private String title;
        private Long travelRequestId;
        private Long costCenterId;
        private List<CreateItemRequest> items;

        public CreateReportRequestBuilder title(String title) { this.title = title; return this; }
        public CreateReportRequestBuilder travelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; return this; }
        public CreateReportRequestBuilder costCenterId(Long costCenterId) { this.costCenterId = costCenterId; return this; }
        public CreateReportRequestBuilder items(List<CreateItemRequest> items) { this.items = items; return this; }

        public CreateReportRequest build() {
            CreateReportRequest obj = new CreateReportRequest();
            obj.setTitle(this.title);
            obj.setTravelRequestId(this.travelRequestId);
            obj.setCostCenterId(this.costCenterId);
            obj.setItems(this.items);
            return obj;
        }
    }
    }

    public static class CreateItemRequest {
        @NotNull
        private ExpenseCategory category;
        @NotNull
        private LocalDate expenseDate;
        @NotBlank
        private String merchantName;
        @NotNull
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String description;
        private String receiptUrl;

    public CreateItemRequest() {}

    public CreateItemRequest(ExpenseCategory category, LocalDate expenseDate, String merchantName, BigDecimal amount, BigDecimal taxAmount, String description, String receiptUrl) {
        this.category = category;
        this.expenseDate = expenseDate;
        this.merchantName = merchantName;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.description = description;
        this.receiptUrl = receiptUrl;
    }

    public ExpenseCategory getCategory() { return category; }

    public void setCategory(ExpenseCategory category) { this.category = category; }

    public LocalDate getExpenseDate() { return expenseDate; }

    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getMerchantName() { return merchantName; }

    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getReceiptUrl() { return receiptUrl; }

    public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }

    public static CreateItemRequestBuilder builder() { return new CreateItemRequestBuilder(); }

    public static class CreateItemRequestBuilder {
        private ExpenseCategory category;
        private LocalDate expenseDate;
        private String merchantName;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String description;
        private String receiptUrl;

        public CreateItemRequestBuilder category(ExpenseCategory category) { this.category = category; return this; }
        public CreateItemRequestBuilder expenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; return this; }
        public CreateItemRequestBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        public CreateItemRequestBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public CreateItemRequestBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public CreateItemRequestBuilder description(String description) { this.description = description; return this; }
        public CreateItemRequestBuilder receiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }

        public CreateItemRequest build() {
            CreateItemRequest obj = new CreateItemRequest();
            obj.setCategory(this.category);
            obj.setExpenseDate(this.expenseDate);
            obj.setMerchantName(this.merchantName);
            obj.setAmount(this.amount);
            obj.setTaxAmount(this.taxAmount);
            obj.setDescription(this.description);
            obj.setReceiptUrl(this.receiptUrl);
            return obj;
        }
    }
    }

    public static class OcrScanResponse {
        private String merchantName;
        private LocalDate date;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String currency;
        private ExpenseCategory suggestedCategory;
        private Boolean policyCompliant;
        private String complianceNote;
        private Boolean duplicateSuspect;

    public OcrScanResponse() {}

    public OcrScanResponse(String merchantName, LocalDate date, BigDecimal amount, BigDecimal taxAmount, String currency, ExpenseCategory suggestedCategory, Boolean policyCompliant, String complianceNote, Boolean duplicateSuspect) {
        this.merchantName = merchantName;
        this.date = date;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.currency = currency;
        this.suggestedCategory = suggestedCategory;
        this.policyCompliant = policyCompliant;
        this.complianceNote = complianceNote;
        this.duplicateSuspect = duplicateSuspect;
    }

    public String getMerchantName() { return merchantName; }

    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency; }

    public ExpenseCategory getSuggestedCategory() { return suggestedCategory; }

    public void setSuggestedCategory(ExpenseCategory suggestedCategory) { this.suggestedCategory = suggestedCategory; }

    public Boolean isPolicyCompliant() { return policyCompliant; }

    public Boolean getPolicyCompliant() { return policyCompliant; }

    public void setPolicyCompliant(Boolean policyCompliant) { this.policyCompliant = policyCompliant; }

    public String getComplianceNote() { return complianceNote; }

    public void setComplianceNote(String complianceNote) { this.complianceNote = complianceNote; }

    public Boolean isDuplicateSuspect() { return duplicateSuspect; }

    public Boolean getDuplicateSuspect() { return duplicateSuspect; }

    public void setDuplicateSuspect(Boolean duplicateSuspect) { this.duplicateSuspect = duplicateSuspect; }

    public static OcrScanResponseBuilder builder() { return new OcrScanResponseBuilder(); }

    public static class OcrScanResponseBuilder {
        private String merchantName;
        private LocalDate date;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String currency;
        private ExpenseCategory suggestedCategory;
        private Boolean policyCompliant;
        private String complianceNote;
        private Boolean duplicateSuspect;

        public OcrScanResponseBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        public OcrScanResponseBuilder date(LocalDate date) { this.date = date; return this; }
        public OcrScanResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public OcrScanResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public OcrScanResponseBuilder currency(String currency) { this.currency = currency; return this; }
        public OcrScanResponseBuilder suggestedCategory(ExpenseCategory suggestedCategory) { this.suggestedCategory = suggestedCategory; return this; }
        public OcrScanResponseBuilder policyCompliant(Boolean policyCompliant) { this.policyCompliant = policyCompliant; return this; }
        public OcrScanResponseBuilder complianceNote(String complianceNote) { this.complianceNote = complianceNote; return this; }
        public OcrScanResponseBuilder duplicateSuspect(Boolean duplicateSuspect) { this.duplicateSuspect = duplicateSuspect; return this; }

        public OcrScanResponse build() {
            OcrScanResponse obj = new OcrScanResponse();
            obj.setMerchantName(this.merchantName);
            obj.setDate(this.date);
            obj.setAmount(this.amount);
            obj.setTaxAmount(this.taxAmount);
            obj.setCurrency(this.currency);
            obj.setSuggestedCategory(this.suggestedCategory);
            obj.setPolicyCompliant(this.policyCompliant);
            obj.setComplianceNote(this.complianceNote);
            obj.setDuplicateSuspect(this.duplicateSuspect);
            return obj;
        }
    }
    }

    public static class ReportResponse {
        private Long id;
        private String reportNumber;
        private String title;
        private Long travelRequestId;
        private String tripName;
        private Long employeeId;
        private String employeeName;
        private String departmentName;
        private ExpenseStatus status;
        private BigDecimal totalAmount;
        private BigDecimal approvedAmount;
        private String currencyCode;
        private Boolean hasAiFlags;
        private String aiReviewNotes;
        private LocalDateTime createdAt;
        private List<ItemResponse> items;

    public ReportResponse() {}

    public ReportResponse(Long id, String reportNumber, String title, Long travelRequestId, String tripName, Long employeeId, String employeeName, String departmentName, ExpenseStatus status, BigDecimal totalAmount, BigDecimal approvedAmount, String currencyCode, Boolean hasAiFlags, String aiReviewNotes, LocalDateTime createdAt, List<ItemResponse> items) {
        this.id = id;
        this.reportNumber = reportNumber;
        this.title = title;
        this.travelRequestId = travelRequestId;
        this.tripName = tripName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.approvedAmount = approvedAmount;
        this.currencyCode = currencyCode;
        this.hasAiFlags = hasAiFlags;
        this.aiReviewNotes = aiReviewNotes;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getReportNumber() { return reportNumber; }

    public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Long getTravelRequestId() { return travelRequestId; }

    public void setTravelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; }

    public String getTripName() { return tripName; }

    public void setTripName(String tripName) { this.tripName = tripName; }

    public Long getEmployeeId() { return employeeId; }

    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }

    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartmentName() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ItemResponse> getItems() { return items; }

    public void setItems(List<ItemResponse> items) { this.items = items; }

    public static ReportResponseBuilder builder() { return new ReportResponseBuilder(); }

    public static class ReportResponseBuilder {
        private Long id;
        private String reportNumber;
        private String title;
        private Long travelRequestId;
        private String tripName;
        private Long employeeId;
        private String employeeName;
        private String departmentName;
        private ExpenseStatus status;
        private BigDecimal totalAmount;
        private BigDecimal approvedAmount;
        private String currencyCode;
        private Boolean hasAiFlags;
        private String aiReviewNotes;
        private LocalDateTime createdAt;
        private List<ItemResponse> items;

        public ReportResponseBuilder id(Long id) { this.id = id; return this; }
        public ReportResponseBuilder reportNumber(String reportNumber) { this.reportNumber = reportNumber; return this; }
        public ReportResponseBuilder title(String title) { this.title = title; return this; }
        public ReportResponseBuilder travelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; return this; }
        public ReportResponseBuilder tripName(String tripName) { this.tripName = tripName; return this; }
        public ReportResponseBuilder employeeId(Long employeeId) { this.employeeId = employeeId; return this; }
        public ReportResponseBuilder employeeName(String employeeName) { this.employeeName = employeeName; return this; }
        public ReportResponseBuilder departmentName(String departmentName) { this.departmentName = departmentName; return this; }
        public ReportResponseBuilder status(ExpenseStatus status) { this.status = status; return this; }
        public ReportResponseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public ReportResponseBuilder approvedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; return this; }
        public ReportResponseBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public ReportResponseBuilder hasAiFlags(Boolean hasAiFlags) { this.hasAiFlags = hasAiFlags; return this; }
        public ReportResponseBuilder aiReviewNotes(String aiReviewNotes) { this.aiReviewNotes = aiReviewNotes; return this; }
        public ReportResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ReportResponseBuilder items(List<ItemResponse> items) { this.items = items; return this; }

        public ReportResponse build() {
            ReportResponse obj = new ReportResponse();
            obj.setId(this.id);
            obj.setReportNumber(this.reportNumber);
            obj.setTitle(this.title);
            obj.setTravelRequestId(this.travelRequestId);
            obj.setTripName(this.tripName);
            obj.setEmployeeId(this.employeeId);
            obj.setEmployeeName(this.employeeName);
            obj.setDepartmentName(this.departmentName);
            obj.setStatus(this.status);
            obj.setTotalAmount(this.totalAmount);
            obj.setApprovedAmount(this.approvedAmount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setHasAiFlags(this.hasAiFlags);
            obj.setAiReviewNotes(this.aiReviewNotes);
            obj.setCreatedAt(this.createdAt);
            obj.setItems(this.items);
            return obj;
        }
    }
    }

    public static class ItemResponse {
        private Long id;
        private ExpenseCategory category;
        private LocalDate expenseDate;
        private String merchantName;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String description;
        private String receiptUrl;
        private Boolean policyCompliant;
        private String policyFlagReason;
        private Boolean duplicateSuspect;

    public ItemResponse() {}

    public ItemResponse(Long id, ExpenseCategory category, LocalDate expenseDate, String merchantName, BigDecimal amount, BigDecimal taxAmount, String description, String receiptUrl, Boolean policyCompliant, String policyFlagReason, Boolean duplicateSuspect) {
        this.id = id;
        this.category = category;
        this.expenseDate = expenseDate;
        this.merchantName = merchantName;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.description = description;
        this.receiptUrl = receiptUrl;
        this.policyCompliant = policyCompliant;
        this.policyFlagReason = policyFlagReason;
        this.duplicateSuspect = duplicateSuspect;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public ExpenseCategory getCategory() { return category; }

    public void setCategory(ExpenseCategory category) { this.category = category; }

    public LocalDate getExpenseDate() { return expenseDate; }

    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getMerchantName() { return merchantName; }

    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getReceiptUrl() { return receiptUrl; }

    public void setReceiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; }

    public Boolean isPolicyCompliant() { return policyCompliant; }

    public Boolean getPolicyCompliant() { return policyCompliant; }

    public void setPolicyCompliant(Boolean policyCompliant) { this.policyCompliant = policyCompliant; }

    public String getPolicyFlagReason() { return policyFlagReason; }

    public void setPolicyFlagReason(String policyFlagReason) { this.policyFlagReason = policyFlagReason; }

    public Boolean isDuplicateSuspect() { return duplicateSuspect; }

    public Boolean getDuplicateSuspect() { return duplicateSuspect; }

    public void setDuplicateSuspect(Boolean duplicateSuspect) { this.duplicateSuspect = duplicateSuspect; }

    public static ItemResponseBuilder builder() { return new ItemResponseBuilder(); }

    public static class ItemResponseBuilder {
        private Long id;
        private ExpenseCategory category;
        private LocalDate expenseDate;
        private String merchantName;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String description;
        private String receiptUrl;
        private Boolean policyCompliant;
        private String policyFlagReason;
        private Boolean duplicateSuspect;

        public ItemResponseBuilder id(Long id) { this.id = id; return this; }
        public ItemResponseBuilder category(ExpenseCategory category) { this.category = category; return this; }
        public ItemResponseBuilder expenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; return this; }
        public ItemResponseBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        public ItemResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public ItemResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public ItemResponseBuilder description(String description) { this.description = description; return this; }
        public ItemResponseBuilder receiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
        public ItemResponseBuilder policyCompliant(Boolean policyCompliant) { this.policyCompliant = policyCompliant; return this; }
        public ItemResponseBuilder policyFlagReason(String policyFlagReason) { this.policyFlagReason = policyFlagReason; return this; }
        public ItemResponseBuilder duplicateSuspect(Boolean duplicateSuspect) { this.duplicateSuspect = duplicateSuspect; return this; }

        public ItemResponse build() {
            ItemResponse obj = new ItemResponse();
            obj.setId(this.id);
            obj.setCategory(this.category);
            obj.setExpenseDate(this.expenseDate);
            obj.setMerchantName(this.merchantName);
            obj.setAmount(this.amount);
            obj.setTaxAmount(this.taxAmount);
            obj.setDescription(this.description);
            obj.setReceiptUrl(this.receiptUrl);
            obj.setPolicyCompliant(this.policyCompliant);
            obj.setPolicyFlagReason(this.policyFlagReason);
            obj.setDuplicateSuspect(this.duplicateSuspect);
            return obj;
        }
    }
    }


    public ExpenseDto() {}

    public static ExpenseDtoBuilder builder() { return new ExpenseDtoBuilder(); }

    public static class ExpenseDtoBuilder {
        public ExpenseDto build() {
            ExpenseDto obj = new ExpenseDto();
            return obj;
        }
    }
}
