package com.sirma.football_api.service;

import com.sirma.football_api.dto.PlayerPairOverlapDto;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.service.interfaces.PlayerPairStatsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlayerPairStatsServiceImpl implements PlayerPairStatsService {

    private final RecordRepository recordRepository;
    private final PlayerRepository playerRepository;

    public PlayerPairStatsServiceImpl(RecordRepository recordRepository, PlayerRepository playerRepository) {
        this.recordRepository = recordRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<PlayerPairOverlapDto> getPlayerPairsByMinutesPlayedTogether(int limit) {
        List<Object[]> rows = recordRepository.findTopPairsWithSubquery(limit);
        Set<Long> playerIds = rows.stream()
                .flatMap(row -> Stream.of(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toSet());
        Map<Long, String> idToName = playerRepository.findAllById(playerIds).stream()
                .collect(Collectors.toMap(Player::getId, Player::getFullName));
        return rows.stream()
                .map(row -> toDto(row, idToName))
                .collect(Collectors.toList());
    }

    private PlayerPairOverlapDto toDto(Object[] row, Map<Long, String> idToName) {
        Long player1Id = ((Number) row[0]).longValue();
        Long player2Id = ((Number) row[1]).longValue();
        Long totalMinutes = ((Number) row[2]).longValue();
        String player1Name = idToName.getOrDefault(player1Id, "ID: " + player1Id);
        String player2Name = idToName.getOrDefault(player2Id, "ID: " + player2Id);
        return new PlayerPairOverlapDto(player1Id, player2Id, totalMinutes, player1Name, player2Name);
    }
}
