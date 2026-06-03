package com.franciscoesquivel.dofuschef.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.TemporalUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder().
                subject(username).
                issuedAt(new Date()).
                expiration(new Date(new Date().getTime() + expiration)).
                signWith(key).
                compact();
    }

    public String getUserFromToken(String token) {
        return Jwts.
                parser().
                verifyWith(key).
                build().
                parseSignedClaims(token).
                getPayload().
                getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
}
