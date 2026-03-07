package com.sirma.football_api.service;

import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.dto.PlayerStatsDto;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Record;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.service.interfaces.PlayerStatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerStatsServiceImpl implements PlayerStatsService {

    private final PlayerRepository playerRepository;
    private final RecordRepository recordRepository;

    public PlayerStatsServiceImpl(PlayerRepository playerRepository, RecordRepository recordRepository) {
        this.playerRepository = playerRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public PlayerStatsDto getPlayerStats(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", playerId));

        List<Record> records = recordRepository.findByPlayerId(playerId);

        Map<Long, Integer> minutesByMatch = new HashMap<>();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (record.getFromMinutes() == null) {
                continue;
            }
            int from = record.getFromMinutes();
            int to = record.getToMinutes() != null ? record.getToMinutes() : 90;
            if (to < from) {
                continue;
            }
            int minutes = to - from;
            Long matchId = record.getMatch().getId();
            Integer current = minutesByMatch.get(matchId);
            if (current == null) {
                minutesByMatch.put(matchId, minutes);
            } else {
                minutesByMatch.put(matchId, current + minutes);
            }
        }

        int matchesPlayed = minutesByMatch.size();
        int totalMinutes = 0;
        List<Integer> minutesValues = new java.util.ArrayList<>(minutesByMatch.values());
        for (int i = 0; i < minutesValues.size(); i++) {
            Integer minutes = minutesValues.get(i);
            totalMinutes += minutes;
        }

        double averageMinutes = 0.0;
        if (matchesPlayed > 0) {
            averageMinutes = (double) totalMinutes / (double) matchesPlayed;
        }

        PlayerStatsDto dto = new PlayerStatsDto();
        dto.setId(player.getId());
        dto.setFullName(player.getFullName());
        dto.setPosition(player.getPosition());
        dto.setTeamNumber(player.getTeamNumber());

        Team team = player.getTeam();
        if (team != null) {
            dto.setTeamId(team.getId());
            dto.setTeamName(team.getName());
            dto.setGroupLetter(team.getGroupLetter());
        }

        dto.setMatchesPlayed(matchesPlayed);
        dto.setTotalMinutes(totalMinutes);
        dto.setAverageMinutesPerMatch(averageMinutes);

        return dto;
    }

    @Override
    public List<PlayerShortDto> getAllPlayers() {
        List<Player> players = playerRepository.findAllWithTeam();
        List<PlayerShortDto> result = new java.util.ArrayList<>(players.size());
        for (Player player : players) {
            PlayerShortDto dto = new PlayerShortDto();
            dto.setId(player.getId());
            dto.setFullName(player.getFullName());
            dto.setPosition(player.getPosition());
            dto.setTeamNumber(player.getTeamNumber());
            if (player.getTeam() != null) {
                dto.setTeamId(player.getTeam().getId());
                dto.setTeamName(player.getTeam().getName());
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public Page<PlayerShortDto> getPlayersPage(Pageable pageable) {
        Sort sort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Direction.ASC, "fullName")
                : pageable.getSort();
        Sort mappedSort = Sort.by(
                sort.stream()
                        .map(order -> {
                            String property = order.getProperty();
                            if ("teamName".equals(property)) {
                                return new Sort.Order(order.getDirection(), "team.name");
                            }
                            return order;
                        })
                        .toList()
        );
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mappedSort);
        return playerRepository.findAll(pageableWithSort).map(this::toPlayerShortDto);
    }

    private PlayerShortDto toPlayerShortDto(Player player) {
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

