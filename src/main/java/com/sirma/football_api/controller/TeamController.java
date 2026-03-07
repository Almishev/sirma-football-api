package com.sirma.football_api.controller;

import com.sirma.football_api.dto.TeamCreateUpdateDto;
import com.sirma.football_api.dto.TeamDetailsDto;
import com.sirma.football_api.dto.TeamSummaryDto;
import com.sirma.football_api.service.interfaces.TeamQueryService;
import com.sirma.football_api.service.interfaces.TeamWriteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216"})
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamQueryService teamQueryService;
    private final TeamWriteService teamWriteService;

    public TeamController(TeamQueryService teamQueryService, TeamWriteService teamWriteService) {
        this.teamQueryService = teamQueryService;
        this.teamWriteService = teamWriteService;
    }

    @GetMapping
    public ResponseEntity<List<TeamSummaryDto>> getAllTeams() {
        log.info("Fetching all teams");
        List<TeamSummaryDto> teams = teamQueryService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDetailsDto> getTeamById(@PathVariable Long id) {
        log.info("Fetching team details for id={}", id);
        TeamDetailsDto team = teamQueryService.getTeamDetails(id);
        return ResponseEntity.ok(team);
    }

    @PostMapping
    public ResponseEntity<TeamSummaryDto> createTeam(@Valid @RequestBody TeamCreateUpdateDto dto) {
        log.info("Creating team: {}", dto.getName());
        TeamSummaryDto created = teamWriteService.create(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri())
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamSummaryDto> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamCreateUpdateDto dto) {
        log.info("Updating team id={}", id);
        TeamSummaryDto updated = teamWriteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        log.info("Deleting team id={}", id);
        teamWriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

