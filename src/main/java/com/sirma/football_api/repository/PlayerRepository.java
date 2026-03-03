package com.sirma.football_api.repository;

import com.sirma.football_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByTeamIdOrderByTeamNumberAsc(Long teamId);
}
