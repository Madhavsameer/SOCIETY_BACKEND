package com.inf.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    // Secret key to sign the JWT token (Use a secure key in production)
	private SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // JWT expiration time (e.g., 1 hour)
    private long jwtExpirationMs = 3600000;

    // Generate JWT token with username and role
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", role)  // Use role without "ROLE_" prefix
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    // Extract username (subject) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract claims from the token
    public <T> T extractClaim(String token, ClaimsExtractor<T> claimsExtractor) {
        final Claims claims = extractAllClaims(token);
        return claimsExtractor.extract(claims);
    }

    // Extract all claims (not including expiration)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate the JWT token (check expiration and if the username matches)
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Get authorities from the token (in case you want to add roles)
    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        final Claims claims = extractAllClaims(token);
        String roles = claims.get("roles", String.class);  // Assuming roles are stored in token claims
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Functional interface to extract claims in a reusable way
    @FunctionalInterface
    public interface ClaimsExtractor<T> {
        T extract(Claims claims);
    }
}
