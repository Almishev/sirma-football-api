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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<MatchSummaryDto> getMatchesPage(Pageable pageable) {
        Sort originalSort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Direction.ASC, "date")
                : pageable.getSort();

        // Map UI sort fields to entity fields
        Sort mappedSort = Sort.by(
                originalSort.stream()
                        .map(order -> {
                            String property = order.getProperty();
                            if ("groupLetter".equals(property)) {
                                // Sort by the group letter of the home team
                                return new Sort.Order(order.getDirection(), "aTeam.groupLetter");
                            }
                            return order;
                        })
                        .toList()
        );

        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mappedSort);

        Page<Match> page = matchRepository.findAll(pageableWithSort);
        return page.map(MatchMapper::toMatchSummaryDto);
    }
}

