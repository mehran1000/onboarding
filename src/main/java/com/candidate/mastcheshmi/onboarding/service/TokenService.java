package com.candidate.mastcheshmi.onboarding.service;

import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * This service class provides functionality for generating and parsing JWT tokens for user authentication.
 */
@Service
public class TokenService {

    /**
     * In the real world, we should use a private key or store the secret in a Kubernetes secret or some vault.
     */
    @Value("${abs-bank.token.secret}")
    private String secret;
    private Key hmacKey;

    @PostConstruct
    public void setUp(){
        hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Generate a JWT token for the provided customer.
     *
     * @param customer The customer for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(Customer customer) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(customer.getUserName())
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
            .signWith(hmacKey)
            .compact();
    }

    /**
     * Get the {@link Authentication} object for the provided JWT token.
     *
     * @param token The JWT token.
     * @return The authentication object containing the user's username if the token is valid, or null if the token is
     * invalid
     */
    public Authentication getAuthentication(String token) {
        if (token != null) {
            Claims body = Jwts.parserBuilder().setSigningKey(hmacKey).build()
                .parseClaimsJws(token)
                .getBody();

            String userName = body.getSubject();

            return userName != null ? new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList())
                : null;
        }
        return null;
    }
}
