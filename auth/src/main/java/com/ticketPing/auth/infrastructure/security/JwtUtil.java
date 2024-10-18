package com.ticketPing.auth.infrastructure.security;

import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import common.exception.ApplicationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_EXPIRATION = 60 * 60 * 1000L;

    private Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String userId, Role role) {
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION))
                        .setIssuedAt(new Date(now.getTime()))
                        .signWith(this.secretKey, SignatureAlgorithm.HS256)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new ApplicationException(AuthErrorCase.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthErrorCase.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new ApplicationException(AuthErrorCase.UNSUPPORTED_AUTHENTICATION);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCase.ILLEGAL_CLAIM);
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}
