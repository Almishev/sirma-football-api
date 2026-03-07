package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.MatchCreateUpdateDto;
import com.sirma.football_api.dto.MatchDetailsDto;

public interface MatchWriteService {

    MatchDetailsDto create(MatchCreateUpdateDto dto);

    MatchDetailsDto update(Long id, MatchCreateUpdateDto dto);

    void deleteById(Long id);
}
