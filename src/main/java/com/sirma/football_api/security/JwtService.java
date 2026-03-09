package com.sirma.football_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret must be at least 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String buildToken(String username, String email, List<String> roleNames, int tokenVersion) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim("email", email != null ? email : "")
                .claim("roles", roleNames)
                .claim("tokenVersion", tokenVersion)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        List<?> list = claims.get("roles", List.class);
        if (list == null) return List.of();
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public int getTokenVersion(Claims claims) {
        Number n = claims.get("tokenVersion", Number.class);
        return n != null ? n.intValue() : 0;
    }

    public String getEmail(Claims claims) {
        String e = claims.get("email", String.class);
        return e != null ? e : "";
    }
}
