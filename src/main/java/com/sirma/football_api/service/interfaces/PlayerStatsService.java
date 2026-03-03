package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.PlayerStatsDto;

public interface PlayerStatsService {

    PlayerStatsDto getPlayerStats(Long playerId);
}

