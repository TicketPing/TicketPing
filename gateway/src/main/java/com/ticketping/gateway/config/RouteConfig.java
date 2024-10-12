package com.ticketping.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth"))

                .route("user-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://user"))

                .route("performance-service", r -> r.path(
                        "/api/v1/performances/**", "api/v1/performance-halls/**")
                        .uri("lb://performance"))

                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .uri("lb://order"))

                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .uri("lb://payment"))

                .route("queue-manage-service", r -> r.path(
                        "/api/waiting-queue", "/api/working-queue")
                        .uri("lb://queue-manage"))

                .route("client-channel-service", r -> r.path("/api/waiting-queue")
                        .uri("lb://client-channel"))
                .build();
    }

}
