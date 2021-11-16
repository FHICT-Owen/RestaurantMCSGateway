package com.example.gateway.filter;

import com.example.gateway.exception.AuthenticationException;
import com.example.gateway.exception.BadCredentialsException;
import com.example.gateway.security.JwtAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Slf4j
@Component
public class AuthenticationFilterFactory extends AbstractGatewayFilterFactory {

    public static final String AUTH_CREDENTIALS = "AUTH_CREDENTIALS";

    private final JwtAuthenticator authenticator;

    public AuthenticationFilterFactory(JwtAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (route != null) {
                boolean success = false;
                if (authenticate(exchange, authenticator)) {
                    success = true;
                }

                if (!success) {
                    throw new AuthenticationException("Authentication failed");
                }
            }

            return chain.filter(exchange);
        });
    }

    private boolean authenticate(ServerWebExchange exchange,
                                 JwtAuthenticator authenticator) {
        boolean success = false;
        Jwt jwt;

        try {
            jwt = authenticator.authenticate(exchange.getRequest());
            exchange.getAttributes().put(AUTH_CREDENTIALS, jwt);
            success = true;
        } catch (BadCredentialsException e) {
            log.info(e.getMessage());
        }

        return success;
    }
}
