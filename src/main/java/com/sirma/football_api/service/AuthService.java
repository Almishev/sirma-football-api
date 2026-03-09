package com.sirma.football_api.service;

import com.sirma.football_api.dto.AuthResponse;
import com.sirma.football_api.dto.ForgotPasswordRequest;
import com.sirma.football_api.dto.LoginRequest;
import com.sirma.football_api.dto.RegisterRequest;
import com.sirma.football_api.dto.ResetPasswordRequest;
import com.sirma.football_api.entity.PasswordResetToken;
import com.sirma.football_api.entity.Role;
import com.sirma.football_api.entity.User;
import com.sirma.football_api.repository.PasswordResetTokenRepository;
import com.sirma.football_api.repository.RoleRepository;
import com.sirma.football_api.repository.UserRepository;
import com.sirma.football_api.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailService mailService;
    private final long resetTokenExpirationMinutes;
    private final String frontendUrl;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       MailService mailService,
                       @Value("${password.reset.token.expiration-minutes:30}") long resetTokenExpirationMinutes,
                       @Value("${app.frontend.url:http://localhost:5173}") String frontendUrl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
        this.resetTokenExpirationMinutes = resetTokenExpirationMinutes;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.findByEmail(request.getEmail().trim()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database. Add roles via SQL."));
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));
        user = userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return buildAuthResponse(user);
    }

    @Transactional
    public void requestPasswordReset(ForgotPasswordRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        if (email.isBlank()) {
            return;
        }
        userRepository.findByEmail(email).ifPresent(user -> {
            passwordResetTokenRepository.deleteByExpiryBefore(Instant.now());
            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setCreatedAt(Instant.now());
            token.setExpiry(Instant.now().plusSeconds(resetTokenExpirationMinutes * 60));
            token.setUsed(false);
            passwordResetTokenRepository.save(token);

            String resetLink = frontendUrl + "/reset-password?token=" + token.getToken();
            mailService.sendPasswordResetEmail(email, resetLink);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String tokenValue = request.getToken();
        PasswordResetToken token = passwordResetTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));
        if (token.isUsed() || token.getExpiry().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired password reset token");
        }
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);
        token.setUsed(true);
        userRepository.save(user);
        passwordResetTokenRepository.save(token);
    }

    @Transactional
    public AuthResponse findOrCreateUserFromGoogle(String email, String name) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        String emailTrimmed = email.trim();
        User user = userRepository.findByEmail(emailTrimmed).orElseGet(() -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database. Add roles via SQL."));
            User newUser = new User();
            String username = userRepository.findByUsername(emailTrimmed).isPresent()
                    ? emailTrimmed.replace("@", "_") + "_" + UUID.randomUUID().toString().substring(0, 8)
                    : emailTrimmed;
            newUser.setUsername(username);
            newUser.setEmail(emailTrimmed);
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.setRoles(Set.of(userRole));
            return userRepository.save(newUser);
        });
        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        String token = jwtService.buildToken(user.getUsername(), user.getEmail(), roleNames, user.getTokenVersion());
        return new AuthResponse(token, user.getUsername(), user.getEmail(), roleNames);
    }
}
