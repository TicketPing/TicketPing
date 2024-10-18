package com.ticketping.gateway.infrastructure.config;

import com.ticketping.gateway.infrastructure.config.filter.QueueCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final QueueCheckFilter queueCheckFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth"))

                .route("user-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://user"))

                .route("performance-service", r -> r.path(
                        "/api/v1/performances/**", "/api/v1/schedules/**", "/api/v1/seats/**")
                        .uri("lb://performance"))

                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://order"))

                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://payment"))

                .route("queue-manage-service", r -> r.path(
                        "/api/v1/waiting-queue/**")
                        .filters(f -> f.filter(queueCheckFilter::filter))
                        .uri("lb://queue-manage"))

                .build();
    }

}
