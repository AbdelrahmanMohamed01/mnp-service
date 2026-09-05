package com.example.mnp.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "porting_requests")
public class PortingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private PortingStatus status;
    private String rejectionReason;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_operator_id")
    private Operator recipient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_operator_id")
    private Operator donor;

    public PortingRequest() {
    }

    public PortingRequest(String phoneNumber, PortingStatus status, Operator recipient, Operator donor) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.recipient = recipient;
        this.donor = donor;
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PortingStatus getStatus() {
        return status;
    }

    public void setStatus(PortingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Operator getRecipient() {
        return recipient;
    }

    public void setRecipient(Operator recipient) {
        this.recipient = recipient;
    }

    public Operator getDonor() {
        return donor;
    }

    public void setDonor(Operator donor) {
        this.donor = donor;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
