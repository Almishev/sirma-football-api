package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;

import java.util.List;

public interface MatchQueryService {

    List<MatchSummaryDto> getAllMatches();

    MatchDetailsDto getMatchDetails(Long id);
}

