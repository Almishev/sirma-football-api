package com.sirma.football_api.service;

import com.sirma.football_api.dto.UserSummaryDto;
import com.sirma.football_api.entity.Role;
import com.sirma.football_api.entity.User;
import com.sirma.football_api.exception.ResourceNotFoundException;
import com.sirma.football_api.repository.RoleRepository;
import com.sirma.football_api.repository.UserRepository;
import com.sirma.football_api.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserSummaryDto> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateRoles(Long id, List<String> roleNames) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void banUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEnabled(false);
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }

    private UserSummaryDto toSummaryDto(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return new UserSummaryDto(user.getId(), user.getUsername(), user.getEmail(), user.isEnabled(), roleNames);
    }
}
