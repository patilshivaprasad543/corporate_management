package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.PaymentMethod;
import com.corporate.travel.entity.enums.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_transactions")

public class PaymentTransaction extends BaseEntity {

    @Column(name = "transaction_reference", nullable = false, unique = true, length = 100)
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private PaymentStatus status = PaymentStatus.SUCCESSFUL;

    @Column(name = "provider_gateway", length = 50)

    private String providerGateway = "STRIPE_ENTERPRISE";

    @Column(name = "gateway_transaction_id", length = 100)
    private String gatewayTransactionId;


    public PaymentTransaction() {}

    public PaymentTransaction(String transactionReference, Booking booking, User user, BigDecimal amount, String currencyCode, PaymentMethod paymentMethod, PaymentStatus status, String providerGateway, String gatewayTransactionId) {
        this.transactionReference = transactionReference;
        this.booking = booking;
        this.user = user;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.providerGateway = providerGateway;
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getTransactionReference() { return transactionReference; }

    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public Booking getBooking() { return booking; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getStatus() { return status; }

    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getProviderGateway() { return providerGateway; }

    public void setProviderGateway(String providerGateway) { this.providerGateway = providerGateway; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }

    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public static PaymentTransactionBuilder builder() { return new PaymentTransactionBuilder(); }

    public static class PaymentTransactionBuilder {
        private Long id;
        private String transactionReference;
        private Booking booking;
        private User user;
        private BigDecimal amount;
        private String currencyCode = "INR";
        private PaymentMethod paymentMethod;
        private PaymentStatus status = PaymentStatus.SUCCESSFUL;
        private String providerGateway = "STRIPE_ENTERPRISE";
        private String gatewayTransactionId;

        public PaymentTransactionBuilder id(Long id) { this.id = id; return this; }
        public PaymentTransactionBuilder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public PaymentTransactionBuilder booking(Booking booking) { this.booking = booking; return this; }
        public PaymentTransactionBuilder user(User user) { this.user = user; return this; }
        public PaymentTransactionBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentTransactionBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public PaymentTransactionBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentTransactionBuilder status(PaymentStatus status) { this.status = status; return this; }
        public PaymentTransactionBuilder providerGateway(String providerGateway) { this.providerGateway = providerGateway; return this; }
        public PaymentTransactionBuilder gatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; return this; }

        public PaymentTransaction build() {
            PaymentTransaction obj = new PaymentTransaction();
            obj.setId(this.id);
            obj.setTransactionReference(this.transactionReference);
            obj.setBooking(this.booking);
            obj.setUser(this.user);
            obj.setAmount(this.amount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setPaymentMethod(this.paymentMethod);
            obj.setStatus(this.status);
            obj.setProviderGateway(this.providerGateway);
            obj.setGatewayTransactionId(this.gatewayTransactionId);
            return obj;
        }
    }
}
