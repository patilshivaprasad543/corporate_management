package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "corporate_cards")

public class CorporateCard extends BaseEntity {

    @Column(name = "card_token", nullable = false, unique = true, length = 100)
    private String cardToken;

    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;

    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderName;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "spending_limit", precision = 12, scale = 2)
    private BigDecimal spendingLimit;

    @Column(name = "current_balance", precision = 12, scale = 2)

    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(name = "is_active")

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;


    public CorporateCard() {}

    public CorporateCard(String cardToken, String lastFourDigits, String cardHolderName, String cardType, LocalDate expiryDate, BigDecimal spendingLimit, BigDecimal currentBalance, Boolean active, User user, Organization organization) {
        this.cardToken = cardToken;
        this.lastFourDigits = lastFourDigits;
        this.cardHolderName = cardHolderName;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.spendingLimit = spendingLimit;
        this.currentBalance = currentBalance;
        this.active = active;
        this.user = user;
        this.organization = organization;
    }

    public String getCardToken() { return cardToken; }

    public void setCardToken(String cardToken) { this.cardToken = cardToken; }

    public String getLastFourDigits() { return lastFourDigits; }

    public void setLastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; }

    public String getCardHolderName() { return cardHolderName; }

    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getCardType() { return cardType; }

    public void setCardType(String cardType) { this.cardType = cardType; }

    public LocalDate getExpiryDate() { return expiryDate; }

    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getSpendingLimit() { return spendingLimit; }

    public void setSpendingLimit(BigDecimal spendingLimit) { this.spendingLimit = spendingLimit; }

    public BigDecimal getCurrentBalance() { return currentBalance; }

    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public static CorporateCardBuilder builder() { return new CorporateCardBuilder(); }

    public static class CorporateCardBuilder {
        private Long id;
        private String cardToken;
        private String lastFourDigits;
        private String cardHolderName;
        private String cardType;
        private LocalDate expiryDate;
        private BigDecimal spendingLimit;
        private BigDecimal currentBalance = BigDecimal.ZERO;
        private Boolean active = true;
        private User user;
        private Organization organization;

        public CorporateCardBuilder id(Long id) { this.id = id; return this; }
        public CorporateCardBuilder cardToken(String cardToken) { this.cardToken = cardToken; return this; }
        public CorporateCardBuilder lastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; return this; }
        public CorporateCardBuilder cardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; return this; }
        public CorporateCardBuilder cardType(String cardType) { this.cardType = cardType; return this; }
        public CorporateCardBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public CorporateCardBuilder spendingLimit(BigDecimal spendingLimit) { this.spendingLimit = spendingLimit; return this; }
        public CorporateCardBuilder currentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; return this; }
        public CorporateCardBuilder active(Boolean active) { this.active = active; return this; }
        public CorporateCardBuilder user(User user) { this.user = user; return this; }
        public CorporateCardBuilder organization(Organization organization) { this.organization = organization; return this; }

        public CorporateCard build() {
            CorporateCard obj = new CorporateCard();
            obj.setId(this.id);
            obj.setCardToken(this.cardToken);
            obj.setLastFourDigits(this.lastFourDigits);
            obj.setCardHolderName(this.cardHolderName);
            obj.setCardType(this.cardType);
            obj.setExpiryDate(this.expiryDate);
            obj.setSpendingLimit(this.spendingLimit);
            obj.setCurrentBalance(this.currentBalance);
            obj.setActive(this.active);
            obj.setUser(this.user);
            obj.setOrganization(this.organization);
            return obj;
        }
    }
}
