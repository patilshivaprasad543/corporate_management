package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itineraries")

public class Itinerary extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_request_id")
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("eventTime ASC")

    private List<ItineraryEvent> events = new ArrayList<>();


    public Itinerary() {}

    public Itinerary(String title, TravelRequest travelRequest, User user, LocalDate startDate, LocalDate endDate, List<ItineraryEvent> events) {
        this.title = title;
        this.travelRequest = travelRequest;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.events = events;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public TravelRequest getTravelRequest() { return travelRequest; }

    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<ItineraryEvent> getEvents() { return events; }

    public void setEvents(List<ItineraryEvent> events) { this.events = events; }

    public static ItineraryBuilder builder() { return new ItineraryBuilder(); }

    public static class ItineraryBuilder {
        private Long id;
        private String title;
        private TravelRequest travelRequest;
        private User user;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<ItineraryEvent> events = new ArrayList<>();

        public ItineraryBuilder id(Long id) { this.id = id; return this; }
        public ItineraryBuilder title(String title) { this.title = title; return this; }
        public ItineraryBuilder travelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; return this; }
        public ItineraryBuilder user(User user) { this.user = user; return this; }
        public ItineraryBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ItineraryBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ItineraryBuilder events(List<ItineraryEvent> events) { this.events = events; return this; }

        public Itinerary build() {
            Itinerary obj = new Itinerary();
            obj.setId(this.id);
            obj.setTitle(this.title);
            obj.setTravelRequest(this.travelRequest);
            obj.setUser(this.user);
            obj.setStartDate(this.startDate);
            obj.setEndDate(this.endDate);
            obj.setEvents(this.events);
            return obj;
        }
    }
}
