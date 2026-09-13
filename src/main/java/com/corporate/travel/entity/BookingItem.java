package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_items")

public class BookingItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "item_type", length = 50)
    private String itemType;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "supplier_reference", length = 100)
    private String supplierReference;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;


    public BookingItem() {}

    public BookingItem(Booking booking, String itemType, String title, String description, String supplierReference, BigDecimal price) {
        this.booking = booking;
        this.itemType = itemType;
        this.title = title;
        this.description = description;
        this.supplierReference = supplierReference;
        this.price = price;
    }

    public Booking getBooking() { return booking; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public String getItemType() { return itemType; }

    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getSupplierReference() { return supplierReference; }

    public void setSupplierReference(String supplierReference) { this.supplierReference = supplierReference; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public static BookingItemBuilder builder() { return new BookingItemBuilder(); }

    public static class BookingItemBuilder {
        private Long id;
        private Booking booking;
        private String itemType;
        private String title;
        private String description;
        private String supplierReference;
        private BigDecimal price;

        public BookingItemBuilder id(Long id) { this.id = id; return this; }
        public BookingItemBuilder booking(Booking booking) { this.booking = booking; return this; }
        public BookingItemBuilder itemType(String itemType) { this.itemType = itemType; return this; }
        public BookingItemBuilder title(String title) { this.title = title; return this; }
        public BookingItemBuilder description(String description) { this.description = description; return this; }
        public BookingItemBuilder supplierReference(String supplierReference) { this.supplierReference = supplierReference; return this; }
        public BookingItemBuilder price(BigDecimal price) { this.price = price; return this; }

        public BookingItem build() {
            BookingItem obj = new BookingItem();
            obj.setId(this.id);
            obj.setBooking(this.booking);
            obj.setItemType(this.itemType);
            obj.setTitle(this.title);
            obj.setDescription(this.description);
            obj.setSupplierReference(this.supplierReference);
            obj.setPrice(this.price);
            return obj;
        }
    }
}
