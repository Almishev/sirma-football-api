package com.sirma.football_api.service;

import com.sirma.football_api.dto.PlayerCreateUpdateDto;
import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.PlayerWriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerWriteServiceImpl implements PlayerWriteService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final RecordRepository recordRepository;

    public PlayerWriteServiceImpl(PlayerRepository playerRepository,
                                 TeamRepository teamRepository,
                                 RecordRepository recordRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    @Transactional
    public PlayerShortDto create(PlayerCreateUpdateDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        Player player = new Player();
        player.setFullName(dto.getFullName().trim());
        player.setPosition(dto.getPosition() != null ? dto.getPosition().trim() : null);
        player.setTeamNumber(dto.getTeamNumber());
        player.setTeam(team);
        Player saved = playerRepository.save(player);
        return toPlayerShortDto(saved);
    }

    @Override
    @Transactional
    public PlayerShortDto update(Long id, PlayerCreateUpdateDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        player.setFullName(dto.getFullName().trim());
        player.setPosition(dto.getPosition() != null ? dto.getPosition().trim() : null);
        player.setTeamNumber(dto.getTeamNumber());
        player.setTeam(team);
        Player saved = playerRepository.save(player);
        return toPlayerShortDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        recordRepository.deleteByPlayerId(id);
        playerRepository.delete(player);
    }

    private static PlayerShortDto toPlayerShortDto(Player player) {
        PlayerShortDto dto = new PlayerShortDto();
        dto.setId(player.getId());
        dto.setFullName(player.getFullName());
        dto.setPosition(player.getPosition());
        dto.setTeamNumber(player.getTeamNumber());
        if (player.getTeam() != null) {
            dto.setTeamId(player.getTeam().getId());
            dto.setTeamName(player.getTeam().getName());
        }
        return dto;
    }
}
