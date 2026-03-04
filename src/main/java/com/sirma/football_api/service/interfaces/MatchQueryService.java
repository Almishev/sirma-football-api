package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchQueryService {

    List<MatchSummaryDto> getAllMatches();

    MatchDetailsDto getMatchDetails(Long id);

    Page<MatchSummaryDto> getMatchesPage(Pageable pageable);
}

