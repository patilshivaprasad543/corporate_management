package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")

public class Invoice extends BaseEntity {

    @Column(name = "invoice_number", nullable = false, unique = true, length = 100)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "payment_status", length = 50)

    private String paymentStatus = "PAID";


    public Invoice() {}

    public Invoice(String invoiceNumber, Booking booking, Vendor vendor, LocalDate invoiceDate, LocalDate dueDate, BigDecimal amount, BigDecimal taxAmount, String paymentStatus) {
        this.invoiceNumber = invoiceNumber;
        this.booking = booking;
        this.vendor = vendor;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.paymentStatus = paymentStatus;
    }

    public String getInvoiceNumber() { return invoiceNumber; }

    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Booking getBooking() { return booking; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public Vendor getVendor() { return vendor; }

    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public LocalDate getInvoiceDate() { return invoiceDate; }

    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDate getDueDate() { return dueDate; }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getPaymentStatus() { return paymentStatus; }

    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public static InvoiceBuilder builder() { return new InvoiceBuilder(); }

    public static class InvoiceBuilder {
        private Long id;
        private String invoiceNumber;
        private Booking booking;
        private Vendor vendor;
        private LocalDate invoiceDate;
        private LocalDate dueDate;
        private BigDecimal amount;
        private BigDecimal taxAmount;
        private String paymentStatus = "PAID";

        public InvoiceBuilder id(Long id) { this.id = id; return this; }
        public InvoiceBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public InvoiceBuilder booking(Booking booking) { this.booking = booking; return this; }
        public InvoiceBuilder vendor(Vendor vendor) { this.vendor = vendor; return this; }
        public InvoiceBuilder invoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public InvoiceBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public InvoiceBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public InvoiceBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public InvoiceBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }

        public Invoice build() {
            Invoice obj = new Invoice();
            obj.setId(this.id);
            obj.setInvoiceNumber(this.invoiceNumber);
            obj.setBooking(this.booking);
            obj.setVendor(this.vendor);
            obj.setInvoiceDate(this.invoiceDate);
            obj.setDueDate(this.dueDate);
            obj.setAmount(this.amount);
            obj.setTaxAmount(this.taxAmount);
            obj.setPaymentStatus(this.paymentStatus);
            return obj;
        }
    }
}
