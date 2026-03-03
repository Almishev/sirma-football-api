package com.sirma.football_api.controller;

import com.sirma.football_api.dto.GroupStandingDto;
import com.sirma.football_api.service.interfaces.GroupStandingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups/standings")
@CrossOrigin(origins = "http://localhost:5173")
public class GroupStandingController {

    private static final Logger log = LoggerFactory.getLogger(GroupStandingController.class);

    private final GroupStandingService groupStandingService;

    public GroupStandingController(GroupStandingService groupStandingService) {
        this.groupStandingService = groupStandingService;
    }

    @GetMapping
    public ResponseEntity<List<GroupStandingDto>> getGroupStandings() {
        log.info("Fetching group standings");
        List<GroupStandingDto> standings = groupStandingService.getGroupStandings();
        return ResponseEntity.ok(standings);
    }
}

