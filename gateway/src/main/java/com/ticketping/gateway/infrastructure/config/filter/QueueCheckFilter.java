package com.ticketping.gateway.infrastructure.config.filter;

import static com.ticketping.gateway.presentation.cases.FilterErrorCase.PERFORMANCE_SOLD_OUT;
import static com.ticketping.gateway.presentation.cases.FilterErrorCase.TOO_MANY_WAITING_USERS;
import static com.ticketping.gateway.presentation.cases.FilterErrorCase.WORKING_TOKEN_NOT_FOUND;

import com.ticketping.gateway.application.service.QueueCheckService;
import com.ticketping.gateway.infrastructure.enums.APIType;
import com.ticketping.gateway.infrastructure.utils.ResponseWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueCheckFilter {

    private final QueueCheckService queueCheckService;
    private final ResponseWriter responseWriter;

    private static final String PERFORMANCE_ID_PARAM = "performanceId";

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String performanceId = exchange.getRequest().getQueryParams().getFirst(PERFORMANCE_ID_PARAM);
        logRequestDetails(exchange, performanceId);

        APIType api = APIType.findByRequest(exchange.getRequest().getURI().getPath(), exchange.getRequest().getMethod().name());

        // TODO: 좌석 선점, 결제 API 추가 (performanceId, userId 필요)
        switch (api) {
            case ENTER_WAITING_QUEUE:
                return handleEnterWaitingQueueAPI(exchange, chain, performanceId);
            case GET_QUEUE_INFO:
                return handleGetQueueTokenAPI(exchange, chain, performanceId);
            case ORDER_PERFORMANCE_SEATS:
                return handleReservationAPI(exchange, chain, performanceId);
            default:
                return chain.filter(exchange);
        }
    }

    private void logRequestDetails(ServerWebExchange exchange, String performanceId) {
        log.info("요청 API 경로: {}, HTTP 메서드: {}, 공연 ID: {}",
                exchange.getRequest().getURI().getPath(),
                exchange.getRequest().getMethod().name(),
                performanceId);
    }

    // 대기열 진입 API
    private Mono<Void> handleEnterWaitingQueueAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        if (queueCheckService.areTooManyWaitingUsers(performanceId)) {
            return responseWriter.setErrorResponse(exchange, TOO_MANY_WAITING_USERS);
        }
        if (queueCheckService.isSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        return chain.filter(exchange);
    }

    // 대기열 상태 조회 API
    private Mono<Void> handleGetQueueTokenAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        if (queueCheckService.isSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        return chain.filter(exchange);
    }

    // 예매 API (좌석 선점, 결제)
    private Mono<Void> handleReservationAPI(ServerWebExchange exchange, GatewayFilterChain chain, String performanceId) {
        if (queueCheckService.isSoldOut(performanceId)) {
            return responseWriter.setErrorResponse(exchange, PERFORMANCE_SOLD_OUT);
        }
        // TODO: Security Context에서 userId 꺼내오기
        String userId = "1";
        if (!queueCheckService.isExistToken(userId, performanceId)) {
            return responseWriter.setErrorResponse(exchange, WORKING_TOKEN_NOT_FOUND);
        }
        return chain.filter(exchange);
    }

}