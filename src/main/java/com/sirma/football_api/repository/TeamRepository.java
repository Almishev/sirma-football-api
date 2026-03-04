package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Modifying
    @Query(value = "ALTER SEQUENCE teams_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
}
