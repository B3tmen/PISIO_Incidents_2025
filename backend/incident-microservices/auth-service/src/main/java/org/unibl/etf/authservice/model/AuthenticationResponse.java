package org.unibl.etf.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserDTO userDTO;
    private String accessToken;
    private String refreshToken;
}
