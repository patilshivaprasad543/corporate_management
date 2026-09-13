package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "itinerary_events")

public class ItineraryEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String location;

    @Column(name = "event_type", length = 50)
    private String eventType;

    @Column(length = 1000)
    private String notes;

    @Column(name = "confirmation_code", length = 100)
    private String confirmationCode;


    public ItineraryEvent() {}

    public ItineraryEvent(Itinerary itinerary, LocalDateTime eventTime, String title, String location, String eventType, String notes, String confirmationCode) {
        this.itinerary = itinerary;
        this.eventTime = eventTime;
        this.title = title;
        this.location = location;
        this.eventType = eventType;
        this.notes = notes;
        this.confirmationCode = confirmationCode;
    }

    public Itinerary getItinerary() { return itinerary; }

    public void setItinerary(Itinerary itinerary) { this.itinerary = itinerary; }

    public LocalDateTime getEventTime() { return eventTime; }

    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public String getConfirmationCode() { return confirmationCode; }

    public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }

    public static ItineraryEventBuilder builder() { return new ItineraryEventBuilder(); }

    public static class ItineraryEventBuilder {
        private Long id;
        private Itinerary itinerary;
        private LocalDateTime eventTime;
        private String title;
        private String location;
        private String eventType;
        private String notes;
        private String confirmationCode;

        public ItineraryEventBuilder id(Long id) { this.id = id; return this; }
        public ItineraryEventBuilder itinerary(Itinerary itinerary) { this.itinerary = itinerary; return this; }
        public ItineraryEventBuilder eventTime(LocalDateTime eventTime) { this.eventTime = eventTime; return this; }
        public ItineraryEventBuilder title(String title) { this.title = title; return this; }
        public ItineraryEventBuilder location(String location) { this.location = location; return this; }
        public ItineraryEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public ItineraryEventBuilder notes(String notes) { this.notes = notes; return this; }
        public ItineraryEventBuilder confirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; return this; }

        public ItineraryEvent build() {
            ItineraryEvent obj = new ItineraryEvent();
            obj.setId(this.id);
            obj.setItinerary(this.itinerary);
            obj.setEventTime(this.eventTime);
            obj.setTitle(this.title);
            obj.setLocation(this.location);
            obj.setEventType(this.eventType);
            obj.setNotes(this.notes);
            obj.setConfirmationCode(this.confirmationCode);
            return obj;
        }
    }
}
