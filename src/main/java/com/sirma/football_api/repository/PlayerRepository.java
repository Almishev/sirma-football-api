package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamIdOrderByTeamNumberAsc(Long teamId);

    @Query("SELECT p FROM Player p JOIN FETCH p.team ORDER BY p.team.id, p.teamNumber")
    List<Player> findAllWithTeam();

    @Modifying
    @Query(value = "ALTER SEQUENCE players_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
}
