package com.ticketping.gateway.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOriginPattern(""); // 모든 origin 허용
        corsConfig.addAllowedHeader(""); // 모든 헤더 허용
        corsConfig.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        corsConfig.setAllowCredentials(true); // 쿠키 허용

        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }
}