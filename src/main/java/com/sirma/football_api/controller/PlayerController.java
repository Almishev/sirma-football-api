package com.sirma.football_api.controller;

import com.sirma.football_api.dto.PlayerCreateUpdateDto;
import com.sirma.football_api.dto.PlayerShortDto;
import com.sirma.football_api.dto.PlayerStatsDto;
import com.sirma.football_api.service.interfaces.PlayerStatsService;
import com.sirma.football_api.service.interfaces.PlayerWriteService;
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

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216", "http://95.216.141.216.nip.io"})
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerStatsService playerStatsService;
    private final PlayerWriteService playerWriteService;

    public PlayerController(PlayerStatsService playerStatsService, PlayerWriteService playerWriteService) {
        this.playerStatsService = playerStatsService;
        this.playerWriteService = playerWriteService;
    }

    @GetMapping
    public ResponseEntity<Page<PlayerShortDto>> getPlayersPage(
            @PageableDefault(size = 15, sort = "fullName") Pageable pageable) {
        log.info("Fetching players page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<PlayerShortDto> page = playerStatsService.getPlayersPage(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerStatsDto> getPlayerStats(@PathVariable Long id) {
        log.info("Fetching player statistics for id={}", id);
        PlayerStatsDto stats = playerStatsService.getPlayerStats(id);
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<PlayerShortDto> createPlayer(@Valid @RequestBody PlayerCreateUpdateDto dto) {
        log.info("Creating player: {}", dto.getFullName());
        PlayerShortDto created = playerWriteService.create(dto);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri())
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerShortDto> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerCreateUpdateDto dto) {
        log.info("Updating player id={}", id);
        PlayerShortDto updated = playerWriteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.info("Deleting player id={}", id);
        playerWriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

