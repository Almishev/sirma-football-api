package com.sirma.football_api.controller;

import com.sirma.football_api.service.DonationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216", "http://95.216.141.216.nip.io"})
public class DonationController {

    private static final Logger log = LoggerFactory.getLogger(DonationController.class);

    private final DonationService donationService;
    private final String stripeWebhookSecret;

    public DonationController(DonationService donationService,
                              @Value("${stripe.webhook-secret}") String stripeWebhookSecret) {
        this.donationService = donationService;
        this.stripeWebhookSecret = stripeWebhookSecret;
    }

    @PostMapping("/donations/create-session")
    public ResponseEntity<Map<String, String>> createSession() throws StripeException {
        Map<String, String> body = donationService.createCheckoutSession();
        return ResponseEntity.ok(body);
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        String payload = sb.toString();

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe webhook signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject obj = deserializer.getObject().orElse(null);
            if (obj instanceof Session checkoutSession) {
                donationService.handleCheckoutCompleted(checkoutSession);
            }
        }

        return ResponseEntity.ok("");
    }
}

