package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")

public class Booking extends BaseEntity {

    @Column(name = "booking_reference", nullable = false, unique = true, length = 50)
    private String bookingReference;

    @Column(name = "pnr_number", length = 50)
    private String pnrNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type", nullable = false)
    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private BookingStatus status = BookingStatus.CONFIRMED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id")
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "is_personal_booking")

    private Boolean personalBooking = false;

    @Column(name = "e_ticket_number", length = 100)
    private String eTicketNumber;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<BookingItem> items = new ArrayList<>();


    public Booking() {}

    public Booking(String bookingReference, String pnrNumber, BookingType bookingType, BookingStatus status, TravelRequest travelRequest, User user, Organization organization, BigDecimal totalAmount, BigDecimal taxAmount, String currencyCode, PaymentMethod paymentMethod, Boolean personalBooking, String eTicketNumber, String cancellationReason, List<BookingItem> items) {
        this.bookingReference = bookingReference;
        this.pnrNumber = pnrNumber;
        this.bookingType = bookingType;
        this.status = status;
        this.travelRequest = travelRequest;
        this.user = user;
        this.organization = organization;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.currencyCode = currencyCode;
        this.paymentMethod = paymentMethod;
        this.personalBooking = personalBooking;
        this.eTicketNumber = eTicketNumber;
        this.cancellationReason = cancellationReason;
        this.items = items;
    }

    public String getBookingReference() { return bookingReference; }

    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getPnrNumber() { return pnrNumber; }

    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public BookingType getBookingType() { return bookingType; }

    public void setBookingType(BookingType bookingType) { this.bookingType = bookingType; }

    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    public TravelRequest getTravelRequest() { return travelRequest; }

    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean isPersonalBooking() { return personalBooking; }

    public Boolean getPersonalBooking() { return personalBooking; }

    public void setPersonalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; }

    public String getETicketNumber() { return eTicketNumber; }

    public void setETicketNumber(String eTicketNumber) { this.eTicketNumber = eTicketNumber; }

    public String getCancellationReason() { return cancellationReason; }

    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public List<BookingItem> getItems() { return items; }

    public void setItems(List<BookingItem> items) { this.items = items; }

    public static BookingBuilder builder() { return new BookingBuilder(); }

    public static class BookingBuilder {
        private Long id;
        private String bookingReference;
        private String pnrNumber;
        private BookingType bookingType;
        private BookingStatus status = BookingStatus.CONFIRMED;
        private TravelRequest travelRequest;
        private User user;
        private Organization organization;
        private BigDecimal totalAmount;
        private BigDecimal taxAmount;
        private String currencyCode = "INR";
        private PaymentMethod paymentMethod;
        private Boolean personalBooking = false;
        private String eTicketNumber;
        private String cancellationReason;
        private List<BookingItem> items = new ArrayList<>();

        public BookingBuilder id(Long id) { this.id = id; return this; }
        public BookingBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public BookingBuilder pnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; return this; }
        public BookingBuilder bookingType(BookingType bookingType) { this.bookingType = bookingType; return this; }
        public BookingBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingBuilder travelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; return this; }
        public BookingBuilder user(User user) { this.user = user; return this; }
        public BookingBuilder organization(Organization organization) { this.organization = organization; return this; }
        public BookingBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public BookingBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public BookingBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public BookingBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public BookingBuilder personalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; return this; }
        public BookingBuilder eTicketNumber(String eTicketNumber) { this.eTicketNumber = eTicketNumber; return this; }
        public BookingBuilder cancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; return this; }
        public BookingBuilder items(List<BookingItem> items) { this.items = items; return this; }

        public Booking build() {
            Booking obj = new Booking();
            obj.setId(this.id);
            obj.setBookingReference(this.bookingReference);
            obj.setPnrNumber(this.pnrNumber);
            obj.setBookingType(this.bookingType);
            obj.setStatus(this.status);
            obj.setTravelRequest(this.travelRequest);
            obj.setUser(this.user);
            obj.setOrganization(this.organization);
            obj.setTotalAmount(this.totalAmount);
            obj.setTaxAmount(this.taxAmount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setPaymentMethod(this.paymentMethod);
            obj.setPersonalBooking(this.personalBooking);
            obj.setETicketNumber(this.eTicketNumber);
            obj.setCancellationReason(this.cancellationReason);
            obj.setItems(this.items);
            return obj;
        }
    }
}
