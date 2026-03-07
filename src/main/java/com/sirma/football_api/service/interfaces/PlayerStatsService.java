package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.dto.PlayerStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerStatsService {

    PlayerStatsDto getPlayerStats(Long playerId);

    List<PlayerShortDto> getAllPlayers();

    Page<PlayerShortDto> getPlayersPage(Pageable pageable);
}

