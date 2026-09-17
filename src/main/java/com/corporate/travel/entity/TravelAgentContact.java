package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "travel_agent_contacts")
public class TravelAgentContact extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @Column(name = "agency_name", nullable = false, length = 200) private String agencyName;
    @Column(name = "agent_name", nullable = false, length = 150) private String agentName;
    @Column(name = "phone_number", nullable = false, length = 40) private String phoneNumber;
    @Column(length = 200) private String email;
    @Column(name = "emergency_phone", length = 40) private String emergencyPhone;
    @Column(length = 500) private String notes;

    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public String getAgencyName() { return agencyName; }
    public void setAgencyName(String agencyName) { this.agencyName = agencyName; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
