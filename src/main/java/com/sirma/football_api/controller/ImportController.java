package com.sirma.football_api.controller;

import com.sirma.football_api.service.interfaces.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/import")
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
}
