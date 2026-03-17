package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    Optional<Donation> findByStripeSessionId(String stripeSessionId);
}

