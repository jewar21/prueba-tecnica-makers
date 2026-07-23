package com.makers.loans.infrastructure.security;

import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.TokenIssuerPort;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class JwtTokenAdapter implements TokenIssuerPort {

    private static final String ISSUER = "loans-api";

    private final JwtEncoder encoder;
    private final Duration expiration;
    private final Clock clock;

    public JwtTokenAdapter(JwtEncoder encoder, Duration expiration, Clock clock) {
        this.encoder = encoder;
        this.expiration = expiration;
        this.clock = clock;
    }

    @Override
    public String issue(User user) {
        Instant issuedAt = clock.instant();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(expiration))
                .subject(user.email())
                .claim("roles", List.of(user.role().name()))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
