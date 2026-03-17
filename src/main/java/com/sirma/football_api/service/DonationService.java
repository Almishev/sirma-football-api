package com.sirma.football_api.service;

import com.sirma.football_api.entity.Donation;
import com.sirma.football_api.repository.DonationRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final String successUrl;
    private final String cancelUrl;

    private static final String DONATION_PRICE_ID = "price_1TByFcP14mNLonrBuSXsL8Se";

    public DonationService(
            DonationRepository donationRepository,
            @Value("${app.frontend.url:http://localhost:5173}") String frontendUrl
    ) {
        this.donationRepository = donationRepository;
        this.successUrl = frontendUrl + "/donate/success";
        this.cancelUrl = frontendUrl + "/donate/cancel";
    }

    @Transactional
    public Map<String, String> createCheckoutSession() throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPrice(DONATION_PRICE_ID)
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);

        Donation donation = new Donation();
        donation.setAmountCents(100L);
        donation.setCurrency("eur");
        donation.setStatus("PENDING");
        donation.setCreatedAt(Instant.now());
        donation.setStripeSessionId(session.getId());
        donationRepository.save(donation);

        Map<String, String> result = new HashMap<>();
        result.put("url", session.getUrl());
        return result;
    }

    @Transactional
    public void handleCheckoutCompleted(Session session) {
        String sessionId = session.getId();
        donationRepository.findByStripeSessionId(sessionId).ifPresent(donation -> {
            donation.setStatus("SUCCEEDED");
            donation.setCompletedAt(Instant.now());
            if (session.getCustomerDetails() != null) {
                donation.setEmail(session.getCustomerDetails().getEmail());
            }
            if (session.getPaymentIntent() != null) {
                donation.setStripePaymentIntentId(session.getPaymentIntent());
            }
            donationRepository.save(donation);
        });
    }
}

