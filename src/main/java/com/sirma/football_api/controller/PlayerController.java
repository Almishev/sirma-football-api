package com.sirma.football_api.controller;

import com.sirma.football_api.dto.PlayerStatsDto;
import com.sirma.football_api.service.interfaces.PlayerStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:5173")
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerStatsService playerStatsService;

    public PlayerController(PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerStatsDto> getPlayerStats(@PathVariable Long id) {
        log.info("Fetching player statistics for id={}", id);
        PlayerStatsDto stats = playerStatsService.getPlayerStats(id);
        return ResponseEntity.ok(stats);
    }
}

