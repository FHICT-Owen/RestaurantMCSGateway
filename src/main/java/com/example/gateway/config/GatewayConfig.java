package com.example.gateway.config;

import com.example.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("menu-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://menu-service"))

                .route("restaurant-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://restaurant-service"))

                .route("session-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://session-service"))
                .build();
    }

}
