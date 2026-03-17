package com.sirma.football_api.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "donations", indexes = {
        @Index(columnList = "stripeSessionId", unique = true)
})
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amountCents;

    @Column(nullable = false, length = 10)
    private String currency;

    private String email;

    @Column(nullable = false, unique = true)
    private String stripeSessionId;

    private String stripePaymentIntentId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant completedAt;

    public Donation() {
    }

    public Long getId() {
        return id;
    }

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStripeSessionId() {
        return stripeSessionId;
    }

    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}

