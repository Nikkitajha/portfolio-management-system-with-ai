package com.internshiproject.portfolioManagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {

    // JWT secret loaded from environment configuration
    @Value("${jwt.secret}")
   private String SECRET;
    

    // Generate signing key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // GENERATE TOKEN
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60)) // 60 hour
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  EXTRACT EMAIL (USERNAME)
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    //  VALIDATE TOKEN
    public boolean validateToken(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    // CHECK IF TOKEN EXPIRED
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    //  GENERIC CLAIM EXTRACTOR
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    //  EXTRACT ALL CLAIMS
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
