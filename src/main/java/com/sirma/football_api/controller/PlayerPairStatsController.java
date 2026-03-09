package com.sirma.football_api.controller;

import com.sirma.football_api.dto.PlayerPairOverlapDto;
import com.sirma.football_api.service.interfaces.PlayerPairStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats/player-pairs")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216", "http://95.216.141.216.nip.io"})
public class PlayerPairStatsController {

    private static final Logger log = LoggerFactory.getLogger(PlayerPairStatsController.class);
    private static final int DEFAULT_LIMIT = 100;

    private final PlayerPairStatsService playerPairStatsService;

    public PlayerPairStatsController(PlayerPairStatsService playerPairStatsService) {
        this.playerPairStatsService = playerPairStatsService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerPairOverlapDto>> getPlayerPairsByMinutesPlayedTogether(
            @RequestParam(defaultValue = "" + DEFAULT_LIMIT) int limit) {
        log.info("Fetching player pairs by minutes played together, limit={}", limit);
        List<PlayerPairOverlapDto> pairs = playerPairStatsService.getPlayerPairsByMinutesPlayedTogether(limit);
        return ResponseEntity.ok(pairs);
    }
}
