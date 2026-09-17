package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "finance_releases")
public class FinanceRelease extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false)
    private TravelRequest travelRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "released_by_user_id")
    private User releasedBy;

    @Column(name = "requested_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal requestedAmount;
    @Column(name = "released_amount", precision = 12, scale = 2)
    private BigDecimal releasedAmount = BigDecimal.ZERO;
    @Column(name = "payment_reference", length = 100) private String paymentReference;
    @Column(length = 500) private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReleaseStatus status = ReleaseStatus.REQUESTED;

    public enum ReleaseStatus { REQUESTED, AUTHORIZED, RELEASED, REJECTED }

    public TravelRequest getTravelRequest(){return travelRequest;} public void setTravelRequest(TravelRequest v){travelRequest=v;}
    public User getRequestedBy(){return requestedBy;} public void setRequestedBy(User v){requestedBy=v;}
    public User getReleasedBy(){return releasedBy;} public void setReleasedBy(User v){releasedBy=v;}
    public BigDecimal getRequestedAmount(){return requestedAmount;} public void setRequestedAmount(BigDecimal v){requestedAmount=v;}
    public BigDecimal getReleasedAmount(){return releasedAmount;} public void setReleasedAmount(BigDecimal v){releasedAmount=v;}
    public String getPaymentReference(){return paymentReference;} public void setPaymentReference(String v){paymentReference=v;}
    public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
    public ReleaseStatus getStatus(){return status;} public void setStatus(ReleaseStatus v){status=v;}
}
