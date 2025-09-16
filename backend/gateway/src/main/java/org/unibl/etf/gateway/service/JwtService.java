package org.unibl.etf.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Service
@RefreshScope
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(
            @Value("${jwt.secret-key}") String secret
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT expired");
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getKey(){
        return secretKey;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
