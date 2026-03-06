package com.sirma.football_api.controller;

import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;
import com.sirma.football_api.service.interfaces.MatchQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216"})
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final MatchQueryService matchQueryService;

    public MatchController(MatchQueryService matchQueryService) {
        this.matchQueryService = matchQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<MatchSummaryDto>> getMatchesPage(
            @PageableDefault(size = 15, sort = "date") Pageable pageable) {
        log.info("Fetching matches page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<MatchSummaryDto> page = matchQueryService.getMatchesPage(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDetailsDto> getMatchById(@PathVariable Long id) {
        log.info("Fetching match details for id={}", id);
        MatchDetailsDto match = matchQueryService.getMatchDetails(id);
        return ResponseEntity.ok(match);
    }
}

