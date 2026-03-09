package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.PlayerPairOverlapDto;

import java.util.List;

public interface PlayerPairStatsService {

    List<PlayerPairOverlapDto> getPlayerPairsByMinutesPlayedTogether(int limit);
}
