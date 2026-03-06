package com.sirma.football_api.controller;

import com.sirma.football_api.dto.TeamDetailsDto;
import com.sirma.football_api.dto.TeamSummaryDto;
import com.sirma.football_api.service.interfaces.TeamQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216"})
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamQueryService teamQueryService;

    public TeamController(TeamQueryService teamQueryService) {
        this.teamQueryService = teamQueryService;
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
}

