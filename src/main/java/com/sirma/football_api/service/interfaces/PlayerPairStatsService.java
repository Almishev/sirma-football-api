package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.PlayerPairOverlapDto;

import java.util.List;

/**
 * Service for the main exam task: pairs of players who played together
 * in the same matches for the longest total time.
 */
public interface PlayerPairStatsService {

    /**
     * Returns pairs of players ordered by total minutes played together (descending).
     *
     * @param limit maximum number of pairs to return (e.g. 10 for top 10)
     */
    List<PlayerPairOverlapDto> getPlayerPairsByMinutesPlayedTogether(int limit);
}
