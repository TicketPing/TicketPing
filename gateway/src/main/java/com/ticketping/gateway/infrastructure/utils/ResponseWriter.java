package com.ticketping.gateway.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketping.gateway.presentation.response.CustomErrorResponse;
import com.ticketping.gateway.presentation.cases.FilterErrorCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ResponseWriter {

    private final ObjectMapper objectMapper;

    public Mono<Void> setErrorResponse(ServerWebExchange exchange, FilterErrorCase filterErrorCase) {
        exchange.getResponse().setStatusCode(filterErrorCase.getHttpStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        CustomErrorResponse errorResponse = new CustomErrorResponse(filterErrorCase.getHttpStatus(), filterErrorCase.getMessage());
        try {
            byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            return exchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(responseBody)));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

}
