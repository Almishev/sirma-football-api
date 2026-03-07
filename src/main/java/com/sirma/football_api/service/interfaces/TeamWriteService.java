package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.TeamCreateUpdateDto;
import com.sirma.football_api.dto.TeamSummaryDto;

public interface TeamWriteService {

    TeamSummaryDto create(TeamCreateUpdateDto dto);

    TeamSummaryDto update(Long id, TeamCreateUpdateDto dto);

    void deleteById(Long id);
}
