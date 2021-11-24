package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/v1/dishes").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/ingredient").permitAll()
                .pathMatchers(HttpMethod.GET, "/register").permitAll()
                .anyExchange().authenticated()
                .and().oauth2ResourceServer()
                .jwt().jwtDecoder(jwtDecoder());
        return http.build();
    }

    ReactiveJwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder) ReactiveJwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }
}
