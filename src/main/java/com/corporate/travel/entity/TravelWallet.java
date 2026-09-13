package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "travel_wallets")

public class TravelWallet extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "allocated_budget", precision = 15, scale = 2)

    private BigDecimal allocatedBudget = BigDecimal.valueOf(300000);

    @Column(name = "approved_budget", precision = 15, scale = 2)

    private BigDecimal approvedBudget = BigDecimal.valueOf(300000);

    @Column(name = "used_budget", precision = 15, scale = 2)

    private BigDecimal usedBudget = BigDecimal.ZERO;

    @Column(name = "pending_expenses", precision = 15, scale = 2)

    private BigDecimal pendingExpenses = BigDecimal.ZERO;

    @Column(name = "reimbursed_amount", precision = 15, scale = 2)

    private BigDecimal reimbursedAmount = BigDecimal.ZERO;

    @Column(name = "corporate_card_limit", precision = 15, scale = 2)

    private BigDecimal corporateCardLimit = BigDecimal.valueOf(200000);

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    public BigDecimal getRemainingBudget() {
        return (allocatedBudget != null ? allocatedBudget : BigDecimal.ZERO)
                .subtract(usedBudget != null ? usedBudget : BigDecimal.ZERO);
    }


    public TravelWallet() {}

    public TravelWallet(User user, BigDecimal allocatedBudget, BigDecimal approvedBudget, BigDecimal usedBudget, BigDecimal pendingExpenses, BigDecimal reimbursedAmount, BigDecimal corporateCardLimit, String currencyCode) {
        this.user = user;
        this.allocatedBudget = allocatedBudget;
        this.approvedBudget = approvedBudget;
        this.usedBudget = usedBudget;
        this.pendingExpenses = pendingExpenses;
        this.reimbursedAmount = reimbursedAmount;
        this.corporateCardLimit = corporateCardLimit;
        this.currencyCode = currencyCode;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public BigDecimal getAllocatedBudget() { return allocatedBudget; }

    public void setAllocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; }

    public BigDecimal getApprovedBudget() { return approvedBudget; }

    public void setApprovedBudget(BigDecimal approvedBudget) { this.approvedBudget = approvedBudget; }

    public BigDecimal getUsedBudget() { return usedBudget; }

    public void setUsedBudget(BigDecimal usedBudget) { this.usedBudget = usedBudget; }

    public BigDecimal getPendingExpenses() { return pendingExpenses; }

    public void setPendingExpenses(BigDecimal pendingExpenses) { this.pendingExpenses = pendingExpenses; }

    public BigDecimal getReimbursedAmount() { return reimbursedAmount; }

    public void setReimbursedAmount(BigDecimal reimbursedAmount) { this.reimbursedAmount = reimbursedAmount; }

    public BigDecimal getCorporateCardLimit() { return corporateCardLimit; }

    public void setCorporateCardLimit(BigDecimal corporateCardLimit) { this.corporateCardLimit = corporateCardLimit; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public static TravelWalletBuilder builder() { return new TravelWalletBuilder(); }

    public static class TravelWalletBuilder {
        private Long id;
        private User user;
        private BigDecimal allocatedBudget = BigDecimal.valueOf(300000);
        private BigDecimal approvedBudget = BigDecimal.valueOf(300000);
        private BigDecimal usedBudget = BigDecimal.ZERO;
        private BigDecimal pendingExpenses = BigDecimal.ZERO;
        private BigDecimal reimbursedAmount = BigDecimal.ZERO;
        private BigDecimal corporateCardLimit = BigDecimal.valueOf(200000);
        private String currencyCode = "INR";

        public TravelWalletBuilder id(Long id) { this.id = id; return this; }
        public TravelWalletBuilder user(User user) { this.user = user; return this; }
        public TravelWalletBuilder allocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; return this; }
        public TravelWalletBuilder approvedBudget(BigDecimal approvedBudget) { this.approvedBudget = approvedBudget; return this; }
        public TravelWalletBuilder usedBudget(BigDecimal usedBudget) { this.usedBudget = usedBudget; return this; }
        public TravelWalletBuilder pendingExpenses(BigDecimal pendingExpenses) { this.pendingExpenses = pendingExpenses; return this; }
        public TravelWalletBuilder reimbursedAmount(BigDecimal reimbursedAmount) { this.reimbursedAmount = reimbursedAmount; return this; }
        public TravelWalletBuilder corporateCardLimit(BigDecimal corporateCardLimit) { this.corporateCardLimit = corporateCardLimit; return this; }
        public TravelWalletBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }

        public TravelWallet build() {
            TravelWallet obj = new TravelWallet();
            obj.setId(this.id);
            obj.setUser(this.user);
            obj.setAllocatedBudget(this.allocatedBudget);
            obj.setApprovedBudget(this.approvedBudget);
            obj.setUsedBudget(this.usedBudget);
            obj.setPendingExpenses(this.pendingExpenses);
            obj.setReimbursedAmount(this.reimbursedAmount);
            obj.setCorporateCardLimit(this.corporateCardLimit);
            obj.setCurrencyCode(this.currencyCode);
            return obj;
        }
    }
}
