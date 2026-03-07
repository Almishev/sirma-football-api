package com.sirma.football_api.controller;

import com.sirma.football_api.dto.MatchCreateUpdateDto;
import com.sirma.football_api.dto.MatchDetailsDto;
import com.sirma.football_api.dto.MatchSummaryDto;
import com.sirma.football_api.service.interfaces.MatchQueryService;
import com.sirma.football_api.service.interfaces.MatchWriteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216"})
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final MatchQueryService matchQueryService;
    private final MatchWriteService matchWriteService;

    public MatchController(MatchQueryService matchQueryService, MatchWriteService matchWriteService) {
        this.matchQueryService = matchQueryService;
        this.matchWriteService = matchWriteService;
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

    @PostMapping
    public ResponseEntity<MatchDetailsDto> createMatch(@Valid @RequestBody MatchCreateUpdateDto dto) {
        log.info("Creating match: {} vs {}", dto.getaTeamId(), dto.getbTeamId());
        MatchDetailsDto created = matchWriteService.create(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri())
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchDetailsDto> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchCreateUpdateDto dto) {
        log.info("Updating match id={}", id);
        MatchDetailsDto updated = matchWriteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        log.info("Deleting match id={}", id);
        matchWriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

