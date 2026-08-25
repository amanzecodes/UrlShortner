package com.urlshortner.UrlShortner.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationTime;


    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public List<String> extractAuthorities(String token) {
        String authorities = parseClaims(token).get("authorities", String.class);
        return authorities == null || authorities.isBlank()
                ? List.of()
                : List.of(authorities.split(","));
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
