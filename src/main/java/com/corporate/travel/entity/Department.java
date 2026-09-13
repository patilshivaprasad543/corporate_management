package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "departments")

public class Department extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "travel_budget", precision = 15, scale = 2)

    private BigDecimal travelBudget = BigDecimal.valueOf(1000000);

    @Column(name = "allocated_budget", precision = 15, scale = 2)

    private BigDecimal allocatedBudget = BigDecimal.valueOf(1000000);

    @Column(name = "spent_budget", precision = 15, scale = 2)

    private BigDecimal spentBudget = BigDecimal.ZERO;


    public Department() {}

    public Department(String name, String code, Organization organization, User manager, BigDecimal travelBudget, BigDecimal allocatedBudget, BigDecimal spentBudget) {
        this.name = name;
        this.code = code;
        this.organization = organization;
        this.manager = manager;
        this.travelBudget = travelBudget;
        this.allocatedBudget = allocatedBudget;
        this.spentBudget = spentBudget;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public User getManager() { return manager; }

    public void setManager(User manager) { this.manager = manager; }

    public BigDecimal getTravelBudget() { return travelBudget; }

    public void setTravelBudget(BigDecimal travelBudget) { this.travelBudget = travelBudget; }

    public BigDecimal getAllocatedBudget() { return allocatedBudget; }

    public void setAllocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; }

    public BigDecimal getSpentBudget() { return spentBudget; }

    public void setSpentBudget(BigDecimal spentBudget) { this.spentBudget = spentBudget; }

    public static DepartmentBuilder builder() { return new DepartmentBuilder(); }

    public static class DepartmentBuilder {
        private Long id;
        private String name;
        private String code;
        private Organization organization;
        private User manager;
        private BigDecimal travelBudget = BigDecimal.valueOf(1000000);
        private BigDecimal allocatedBudget = BigDecimal.valueOf(1000000);
        private BigDecimal spentBudget = BigDecimal.ZERO;

        public DepartmentBuilder id(Long id) { this.id = id; return this; }
        public DepartmentBuilder name(String name) { this.name = name; return this; }
        public DepartmentBuilder code(String code) { this.code = code; return this; }
        public DepartmentBuilder organization(Organization organization) { this.organization = organization; return this; }
        public DepartmentBuilder manager(User manager) { this.manager = manager; return this; }
        public DepartmentBuilder travelBudget(BigDecimal travelBudget) { this.travelBudget = travelBudget; return this; }
        public DepartmentBuilder allocatedBudget(BigDecimal allocatedBudget) { this.allocatedBudget = allocatedBudget; return this; }
        public DepartmentBuilder spentBudget(BigDecimal spentBudget) { this.spentBudget = spentBudget; return this; }

        public Department build() {
            Department obj = new Department();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setCode(this.code);
            obj.setOrganization(this.organization);
            obj.setManager(this.manager);
            obj.setTravelBudget(this.travelBudget);
            obj.setAllocatedBudget(this.allocatedBudget);
            obj.setSpentBudget(this.spentBudget);
            return obj;
        }
    }
}
