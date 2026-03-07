package com.sirma.football_api.service;

import com.sirma.football_api.dto.TeamCreateUpdateDto;
import com.sirma.football_api.dto.TeamSummaryDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.MatchRepository;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.TeamWriteService;
import com.sirma.football_api.util.TeamMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamWriteServiceImpl implements TeamWriteService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final RecordRepository recordRepository;
    private final MatchRepository matchRepository;

    public TeamWriteServiceImpl(TeamRepository teamRepository,
                                PlayerRepository playerRepository,
                                RecordRepository recordRepository,
                                MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.recordRepository = recordRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public TeamSummaryDto create(TeamCreateUpdateDto dto) {
        Team team = new Team();
        team.setName(dto.getName().trim());
        team.setManagerFullName(dto.getManagerFullName() != null ? dto.getManagerFullName().trim() : null);
        team.setGroupLetter(dto.getGroupLetter() != null ? dto.getGroupLetter().trim() : null);
        Team saved = teamRepository.save(team);
        return TeamMapper.toTeamSummaryDto(saved);
    }

    @Override
    @Transactional
    public TeamSummaryDto update(Long id, TeamCreateUpdateDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
        team.setName(dto.getName().trim());
        team.setManagerFullName(dto.getManagerFullName() != null ? dto.getManagerFullName().trim() : null);
        team.setGroupLetter(dto.getGroupLetter() != null ? dto.getGroupLetter().trim() : null);
        Team saved = teamRepository.save(team);
        return TeamMapper.toTeamSummaryDto(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
        List<Match> matches = matchRepository.findByATeamIdOrBTeamId(id);
        for (Match match : matches) {
            recordRepository.deleteByMatchId(match.getId());
            matchRepository.delete(match);
        }
        List<Player> players = playerRepository.findByTeamIdOrderByTeamNumberAsc(id);
        List<Long> playerIds = players.stream().map(Player::getId).collect(Collectors.toList());
        if (!playerIds.isEmpty()) {
            recordRepository.deleteByPlayerIdIn(playerIds);
        }
        playerRepository.deleteAll(players);
        teamRepository.delete(team);
    }
}
