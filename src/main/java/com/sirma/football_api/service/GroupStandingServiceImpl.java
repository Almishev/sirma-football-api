package com.sirma.football_api.service;

import com.sirma.football_api.dto.GroupStandingDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.repository.MatchRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.GroupStandingService;
import com.sirma.football_api.util.GroupStandingsCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupStandingServiceImpl implements GroupStandingService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public GroupStandingServiceImpl(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<GroupStandingDto> getGroupStandings() {
        List<Team> teams = teamRepository.findAll();
        List<Match> matches = matchRepository.findAll();
        return GroupStandingsCalculator.calculateStandings(teams, matches);
    }
}

