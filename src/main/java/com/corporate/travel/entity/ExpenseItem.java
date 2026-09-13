package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.ExpenseCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense_items")

public class ExpenseItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_report_id", nullable = false)
    private ExpenseReport expenseReport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "merchant_name", nullable = false, length = 150)
    private String merchantName;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "tax_amount", precision = 10, scale = 2)

    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Column(length = 500)
    private String description;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(name = "is_policy_compliant")

    private Boolean policyCompliant = true;

    @Column(name = "policy_flag_reason", length = 500)
    private String policyFlagReason;

    @Column(name = "is_duplicate_suspect")

    private Boolean duplicateSuspect = false;


    public ExpenseItem() {}

    public ExpenseItem(ExpenseReport expenseReport, ExpenseCategory category, LocalDate expenseDate, String merchantName, BigDecimal amount, BigDecimal taxAmount, String currencyCode, String description, String receiptUrl, Boolean policyCompliant, String policyFlagReason, Boolean duplicateSuspect) {
        this.expenseReport = expenseReport;
        this.category = category;
        this.expenseDate = expenseDate;
        this.merchantName = merchantName;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.currencyCode = currencyCode;
        this.description = description;
        this.receiptUrl = receiptUrl;
        this.policyCompliant = policyCompliant;
        this.policyFlagReason = policyFlagReason;
        this.duplicateSuspect = duplicateSuspect;
    }

    public ExpenseReport getExpenseReport() { return expenseReport; }

    public void setExpenseReport(ExpenseReport expenseReport) { this.expenseReport = expenseReport; }

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

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

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

    public static ExpenseItemBuilder builder() { return new ExpenseItemBuilder(); }

    public static class ExpenseItemBuilder {
        private Long id;
        private ExpenseReport expenseReport;
        private ExpenseCategory category;
        private LocalDate expenseDate;
        private String merchantName;
        private BigDecimal amount;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private String currencyCode = "INR";
        private String description;
        private String receiptUrl;
        private Boolean policyCompliant = true;
        private String policyFlagReason;
        private Boolean duplicateSuspect = false;

        public ExpenseItemBuilder id(Long id) { this.id = id; return this; }
        public ExpenseItemBuilder expenseReport(ExpenseReport expenseReport) { this.expenseReport = expenseReport; return this; }
        public ExpenseItemBuilder category(ExpenseCategory category) { this.category = category; return this; }
        public ExpenseItemBuilder expenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; return this; }
        public ExpenseItemBuilder merchantName(String merchantName) { this.merchantName = merchantName; return this; }
        public ExpenseItemBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public ExpenseItemBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public ExpenseItemBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public ExpenseItemBuilder description(String description) { this.description = description; return this; }
        public ExpenseItemBuilder receiptUrl(String receiptUrl) { this.receiptUrl = receiptUrl; return this; }
        public ExpenseItemBuilder policyCompliant(Boolean policyCompliant) { this.policyCompliant = policyCompliant; return this; }
        public ExpenseItemBuilder policyFlagReason(String policyFlagReason) { this.policyFlagReason = policyFlagReason; return this; }
        public ExpenseItemBuilder duplicateSuspect(Boolean duplicateSuspect) { this.duplicateSuspect = duplicateSuspect; return this; }

        public ExpenseItem build() {
            ExpenseItem obj = new ExpenseItem();
            obj.setId(this.id);
            obj.setExpenseReport(this.expenseReport);
            obj.setCategory(this.category);
            obj.setExpenseDate(this.expenseDate);
            obj.setMerchantName(this.merchantName);
            obj.setAmount(this.amount);
            obj.setTaxAmount(this.taxAmount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setDescription(this.description);
            obj.setReceiptUrl(this.receiptUrl);
            obj.setPolicyCompliant(this.policyCompliant);
            obj.setPolicyFlagReason(this.policyFlagReason);
            obj.setDuplicateSuspect(this.duplicateSuspect);
            return obj;
        }
    }
}
