package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/v1/dish").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/ingredient").permitAll()
                .pathMatchers(HttpMethod.GET, "/register").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
}
