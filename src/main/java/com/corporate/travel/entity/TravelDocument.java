package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "travel_documents")
public class TravelDocument extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 30)
    private DocumentType documentType;

    @Column(nullable = false, length = 255) private String fileName;
    @Column(nullable = false, length = 500) private String storageKey;
    @Column(length = 1000) private String description;
    @Column(name = "visible_to_employee", nullable = false) private boolean visibleToEmployee = true;

    public enum DocumentType { FLIGHT_TICKET, HOTEL_VOUCHER, CAB_CONFIRMATION, TRAIN_TICKET, ITINERARY, OTHER }

    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isVisibleToEmployee() { return visibleToEmployee; }
    public void setVisibleToEmployee(boolean visibleToEmployee) { this.visibleToEmployee = visibleToEmployee; }
}
