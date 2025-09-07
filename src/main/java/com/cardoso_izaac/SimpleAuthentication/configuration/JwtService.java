package com.cardoso_izaac.SimpleAuthentication.configuration;

import com.cardoso_izaac.SimpleAuthentication.dto.SignInDTO;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(SignInDTO request) {
        var now = Instant.now();
        long expire = 36000L;

        var claims = JwtClaimsSet.builder()
                .issuer("SimpleAuthentication")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expire))
                .subject(request.email())
                .subject(request.password())
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
