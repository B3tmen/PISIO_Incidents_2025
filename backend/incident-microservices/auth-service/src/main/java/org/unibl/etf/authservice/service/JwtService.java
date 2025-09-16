package org.unibl.etf.authservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.unibl.etf.authservice.model.UserDTO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;

@Service
@RefreshScope
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    @Getter
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret-key}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(UserDTO user) {
        return buildToken(user, accessTokenExpirationMs);
    }

    public String generateRefreshToken(UserDTO user) {
        return buildToken(user, refreshTokenExpirationMs);
    }

    private String buildToken(UserDTO user, long expirationMs) {
        Map<String, String> claims = Stream.of(
                        entry("email", user.getEmail()),
                        entry("givenName", user.getGivenName() != null ? user.getGivenName() : ""),
                        entry("familyName", user.getGivenName() != null ? user.getGivenName() : ""),
                        entry("picture", user.getGivenName() != null ? user.getGivenName() : "")
                )
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .issuer("PISIO-auth-service")
                .subject(UUID.randomUUID().toString())
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + expirationMs))
                .id(UUID.randomUUID().toString())

                .claims().add(claims)

                .and()
                .signWith(getKey())
                .compact();
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
        return extractClaim(token, claims -> claims.get("email", String.class));
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

    private boolean isExpired(String token) {
        try{
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();

            return expiration != null && expiration.before(new Date());
        } catch (ExpiredJwtException e){
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
