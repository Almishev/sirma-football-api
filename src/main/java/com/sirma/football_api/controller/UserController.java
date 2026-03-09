package com.sirma.football_api.controller;

import com.sirma.football_api.dto.UpdateRolesRequest;
import com.sirma.football_api.dto.UserSummaryDto;
import com.sirma.football_api.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://95.216.141.216", "http://95.216.141.216.nip.io"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryDto>> listUsers() {
        log.debug("List users requested");
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<Void> updateRoles(@PathVariable Long id,
                                           @Valid @RequestBody UpdateRolesRequest request) {
        log.info("Updating roles for user id: {} to {}", id, request.getRoleNames());
        userService.updateRoles(id, request.getRoleNames());
        log.info("Roles updated for user id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        log.info("Ban user requested for id: {}", id);
        userService.banUser(id);
        log.info("User banned successfully, id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
