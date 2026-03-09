package com.sirma.football_api.controller;

import com.sirma.football_api.service.interfaces.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216", "http://95.216.141.216.nip.io"})
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping(value = "/teams", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Integer>> importTeams(
            @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Importing file: {}", file.getOriginalFilename());
        int count = importService.importTeams(file);
        log.info("Imported {} records from file: {}", count, file.getOriginalFilename());
        return ResponseEntity.ok(Map.of("imported", count));
    }

    @PostMapping(value = "/players", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Integer>> importPlayers(
            @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Importing file: {}", file.getOriginalFilename());
        int count = importService.importPlayers(file);
        log.info("Imported {} records from file: {}", count, file.getOriginalFilename());
        return ResponseEntity.ok(Map.of("imported", count));
    }

    @PostMapping(value = "/matches", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Integer>> importMatches(
            @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Importing file: {}", file.getOriginalFilename());
        int count = importService.importMatches(file);
        log.info("Imported {} records from file: {}", count, file.getOriginalFilename());
        return ResponseEntity.ok(Map.of("imported", count));
    }

    @PostMapping(value = "/records", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Integer>> importRecords(
            @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Importing file: {}", file.getOriginalFilename());
        int count = importService.importRecords(file);
        log.info("Imported {} records from file: {}", count, file.getOriginalFilename());
        return ResponseEntity.ok(Map.of("imported", count));
    }

    @DeleteMapping("/records")
    public ResponseEntity<Map<String, Integer>> deleteRecords() {
        int count = importService.deleteRecords();
        log.info("Deleted {} records", count);
        return ResponseEntity.ok(Map.of("deleted", count));
    }

    @DeleteMapping("/matches")
    public ResponseEntity<Map<String, Integer>> deleteMatches() {
        int count = importService.deleteMatches();
        log.info("Deleted {} matches", count);
        return ResponseEntity.ok(Map.of("deleted", count));
    }

    @DeleteMapping("/players")
    public ResponseEntity<Map<String, Integer>> deletePlayers() {
        int count = importService.deletePlayers();
        log.info("Deleted {} players", count);
        return ResponseEntity.ok(Map.of("deleted", count));
    }

    @DeleteMapping("/teams")
    public ResponseEntity<Map<String, Integer>> deleteTeams() {
        int count = importService.deleteTeams();
        log.info("Deleted {} teams", count);
        return ResponseEntity.ok(Map.of("deleted", count));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Map<String, Integer>> deleteAll() {
        Map<String, Integer> counts = importService.deleteAll();
        log.info("Deleted all data: {}", counts);
        return ResponseEntity.ok(counts);
    }
}
