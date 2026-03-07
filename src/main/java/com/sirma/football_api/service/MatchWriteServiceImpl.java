package com.sirma.football_api.service;

import com.sirma.football_api.dto.MatchCreateUpdateDto;
import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Record;
import com.sirma.football_api.entity.Team;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.MatchRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.repository.TeamRepository;
import com.sirma.football_api.service.interfaces.MatchWriteService;
import com.sirma.football_api.util.MatchMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MatchWriteServiceImpl implements MatchWriteService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final RecordRepository recordRepository;

    public MatchWriteServiceImpl(MatchRepository matchRepository,
                                 TeamRepository teamRepository,
                                 RecordRepository recordRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    @Transactional
    public MatchDetailsDto create(MatchCreateUpdateDto dto) {
        validateTeamIds(dto.getaTeamId(), dto.getbTeamId());
        Team aTeam = teamRepository.findById(dto.getaTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getaTeamId()));
        Team bTeam = teamRepository.findById(dto.getbTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getbTeamId()));
        validateSameGroup(aTeam, bTeam);
        Match match = new Match();
        match.setaTeam(aTeam);
        match.setbTeam(bTeam);
        match.setDate(dto.getDate());
        match.setScore(dto.getScore() != null ? dto.getScore().trim() : null);
        Match saved = matchRepository.save(match);
        return getMatchDetailsDto(saved.getId());
    }

    @Override
    @Transactional
    public MatchDetailsDto update(Long id, MatchCreateUpdateDto dto) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        validateTeamIds(dto.getaTeamId(), dto.getbTeamId());
        Team aTeam = teamRepository.findById(dto.getaTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getaTeamId()));
        Team bTeam = teamRepository.findById(dto.getbTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getbTeamId()));
        validateSameGroup(aTeam, bTeam);
        match.setaTeam(aTeam);
        match.setbTeam(bTeam);
        match.setDate(dto.getDate());
        match.setScore(dto.getScore() != null ? dto.getScore().trim() : null);
        matchRepository.save(match);
        return getMatchDetailsDto(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        recordRepository.deleteByMatchId(id);
        matchRepository.delete(match);
    }

    private void validateTeamIds(Long aTeamId, Long bTeamId) {
        if (aTeamId != null && aTeamId.equals(bTeamId)) {
            throw new IllegalArgumentException("aTeamId and bTeamId must be different");
        }
    }

    private void validateSameGroup(Team aTeam, Team bTeam) {
        if (!Objects.equals(aTeam.getGroupLetter(), bTeam.getGroupLetter())) {
            throw new IllegalArgumentException("Both teams must be in the same group");
        }
    }

    private MatchDetailsDto getMatchDetailsDto(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));
        List<Record> records = recordRepository.findByMatchId(matchId);
        return MatchMapper.toMatchDetailsDto(match, records);
    }
}
