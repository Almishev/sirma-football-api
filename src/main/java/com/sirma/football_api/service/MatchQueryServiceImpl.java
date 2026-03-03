package com.sirma.football_api.service;

import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;
import com.sirma.football_api.service.interfaces.MatchQueryService;
import com.sirma.football_api.entity.Match;
import com.sirma.football_api.entity.Record;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.MatchRepository;
import com.sirma.football_api.repository.RecordRepository;
import com.sirma.football_api.util.MatchMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchQueryServiceImpl implements MatchQueryService {

    private final MatchRepository matchRepository;
    private final RecordRepository recordRepository;

    public MatchQueryServiceImpl(MatchRepository matchRepository, RecordRepository recordRepository) {
        this.matchRepository = matchRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public List<MatchSummaryDto> getAllMatches() {
        List<Match> matches = matchRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        List<MatchSummaryDto> result = new ArrayList<>(matches.size());
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            result.add(MatchMapper.toMatchSummaryDto(match));
        }
        return result;
    }

    @Override
    public MatchDetailsDto getMatchDetails(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        List<Record> records = recordRepository.findByMatchId(id);
        return MatchMapper.toMatchDetailsDto(match, records);
    }
}

