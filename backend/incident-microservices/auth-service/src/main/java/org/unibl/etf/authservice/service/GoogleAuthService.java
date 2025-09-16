package org.unibl.etf.authservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.unibl.etf.authservice.model.AuthenticationResponse;
import org.unibl.etf.authservice.model.UserDTO;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@Service
public class GoogleAuthService {
    private final JwtService jwtService;
    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService(JwtService jwtService, @Value("${oauth2.client.registration.google.client-id}") String clientId) {
        this.jwtService = jwtService;
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public AuthenticationResponse authenticateWithGoogle(String idTokenString) throws GeneralSecurityException, IOException  {
        GoogleIdToken.Payload payload = verify(idTokenString);

        if (payload == null || payload.getEmail() == null) {
            throw new RuntimeException("Invalid Google token");
        }

        System.out.println("PAYLOAD: " + payload.toString());

        String email = (String) payload.get("email");
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String picture = (String) payload.get("picture");

        UserDTO user = new UserDTO(email, firstName, lastName, picture);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(user, accessToken, refreshToken);
    }

    public GoogleIdToken.Payload verify(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        }

        return null;
    }
}
