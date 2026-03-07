package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE m.aTeam.id = :teamId OR m.bTeam.id = :teamId")
    List<Match> findByATeamIdOrBTeamId(Long teamId);

    @Modifying
    @Query(value = "ALTER SEQUENCE matches_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
}
