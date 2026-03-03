package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.TeamSummaryDto;
import com.sirma.football_api.dto.TeamDetailsDto;

import java.util.List;

public interface TeamQueryService {

    List<TeamSummaryDto> getAllTeams();

    TeamDetailsDto getTeamDetails(Long id);
}

