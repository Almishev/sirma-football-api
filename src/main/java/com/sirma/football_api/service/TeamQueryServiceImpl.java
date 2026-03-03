package com.sirma.football_api.service;

import com.sirma.football_api.dto.TeamDetailsDto;
import com.sirma.football_api.dto.TeamSummaryDto;
import com.sirma.football_api.entity.Player;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.PlayerRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.TeamQueryService;
import com.sirma.football_api.util.TeamMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamQueryServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<TeamSummaryDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        List<TeamSummaryDto> result = new ArrayList<>(teams.size());
        for (Team team : teams) {
            TeamSummaryDto dto = new TeamSummaryDto();
            dto.setId(team.getId());
            dto.setName(team.getName());
            dto.setGroupLetter(team.getGroupLetter());
            result.add(dto);
        }
        return result;
    }

    @Override
    public TeamDetailsDto getTeamDetails(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        List<Player> players = playerRepository.findByTeamIdOrderByTeamNumberAsc(id);
        return TeamMapper.toTeamDetailsDto(team, players);
    }
}

