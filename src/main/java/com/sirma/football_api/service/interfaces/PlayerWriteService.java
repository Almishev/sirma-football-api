package com.sirma.football_api.service.interfaces;

import com.sirma.football_api.dto.PlayerCreateUpdateDto;
import com.sirma.football_api.dto.PlayerShortDto;

public interface PlayerWriteService {

    PlayerShortDto create(PlayerCreateUpdateDto dto);

    PlayerShortDto update(Long id, PlayerCreateUpdateDto dto);

    void deleteById(Long id);
}
