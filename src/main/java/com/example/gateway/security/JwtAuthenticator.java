package com.example.gateway.security;

import com.example.gateway.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticator {

    private final JwtExtractor extractor;

    private final JwtDecoder decoder;

    @Autowired
    public JwtAuthenticator(JwtExtractor extractor, JwtDecoder decoder) {
        this.extractor = extractor;
        this.decoder = decoder;
    }

    public Jwt authenticate(ServerHttpRequest request) {
        String token = extractor.extract(request);

        try {
            return decoder.decode(token);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new AuthenticationException("Invalid token!");
        }
    }
}
