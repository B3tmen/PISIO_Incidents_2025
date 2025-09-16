package org.unibl.etf.authservice.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.authservice.model.AuthenticationResponse;
import org.unibl.etf.authservice.model.UserDTO;
import org.unibl.etf.authservice.service.GoogleAuthService;
import org.unibl.etf.authservice.service.JwtService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RefreshScope
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Log4j2
public class AuthController {
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    public AuthController(GoogleAuthService googleAuthService, JwtService jwtService) {
        this.googleAuthService = googleAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/google/login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String,String> body) {
        String token = body.get("token");
        System.out.println("TOKEN: " + token);
        try{
            AuthenticationResponse authResponse = googleAuthService.authenticateWithGoogle(token);
            ResponseCookie newRefreshCookie = getRefreshCookie(authResponse.getRefreshToken());

            log.info("[Auth] COOKIE: {}", newRefreshCookie);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                    .body(Map.of("accessToken", authResponse.getAccessToken()));
        } catch (GeneralSecurityException | IOException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        // Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return jwtService.isTokenValid(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bearer token is missing.");
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }

        String email = jwtService.extractEmail(refreshToken);

        // You might load user details from DB here
        UserDTO user = new UserDTO(email, null, null, null);
        String newAccessToken = jwtService.generateAccessToken(user);
        log.info("[JWT] Generated new access token: {}\n", newAccessToken);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    private ResponseCookie getRefreshCookie(String refreshToken) {
        System.out.println("Cookie age: " + jwtService.getRefreshTokenExpirationMs());
        Duration duration = Duration.ofMillis(jwtService.getRefreshTokenExpirationMs());

        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)     // No XSS through javascript
                .path("/api/v1/auth")
                .maxAge(duration)
                .sameSite("Lax") // For CSRF
                .build();
    }

}
