package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cost_centers")

public class CostCenter extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "budget_limit", precision = 15, scale = 2)

    private BigDecimal budgetLimit = BigDecimal.valueOf(500000);

    @Column(name = "current_spend", precision = 15, scale = 2)

    private BigDecimal currentSpend = BigDecimal.ZERO;


    public CostCenter() {}

    public CostCenter(String name, String code, Organization organization, BigDecimal budgetLimit, BigDecimal currentSpend) {
        this.name = name;
        this.code = code;
        this.organization = organization;
        this.budgetLimit = budgetLimit;
        this.currentSpend = currentSpend;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public BigDecimal getBudgetLimit() { return budgetLimit; }

    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }

    public BigDecimal getCurrentSpend() { return currentSpend; }

    public void setCurrentSpend(BigDecimal currentSpend) { this.currentSpend = currentSpend; }

    public static CostCenterBuilder builder() { return new CostCenterBuilder(); }

    public static class CostCenterBuilder {
        private Long id;
        private String name;
        private String code;
        private Organization organization;
        private BigDecimal budgetLimit = BigDecimal.valueOf(500000);
        private BigDecimal currentSpend = BigDecimal.ZERO;

        public CostCenterBuilder id(Long id) { this.id = id; return this; }
        public CostCenterBuilder name(String name) { this.name = name; return this; }
        public CostCenterBuilder code(String code) { this.code = code; return this; }
        public CostCenterBuilder organization(Organization organization) { this.organization = organization; return this; }
        public CostCenterBuilder budgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; return this; }
        public CostCenterBuilder currentSpend(BigDecimal currentSpend) { this.currentSpend = currentSpend; return this; }

        public CostCenter build() {
            CostCenter obj = new CostCenter();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setCode(this.code);
            obj.setOrganization(this.organization);
            obj.setBudgetLimit(this.budgetLimit);
            obj.setCurrentSpend(this.currentSpend);
            return obj;
        }
    }
}
