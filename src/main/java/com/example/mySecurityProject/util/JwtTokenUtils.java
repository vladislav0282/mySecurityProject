package com.example.mySecurityProject.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtTokenUtils {
    //private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private Key key;

    @Value("${jjwt.secret}")
    private String secret;

    @Value("${jjwt.access}")
    private Duration access;

    @Value("${jjwt.refresh}")
    private Duration refresh;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);
        // если хотим еще какое нибудь поле добавть то например claims.put("email", emailList);

        Date issuedDate = new Date();
        Date expiryDate = new Date(issuedDate.getTime() + access.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512) // Use the secure key
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date issuedDate = new Date();
        Date expiryDate = new Date(issuedDate.getTime() + refresh.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512) // Use the secure key
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        return getAllClaimsFromRefreshToken(refreshToken).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims getAllClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(key).build()
                .parseSignedClaims(refreshToken)
                .getPayload();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}
