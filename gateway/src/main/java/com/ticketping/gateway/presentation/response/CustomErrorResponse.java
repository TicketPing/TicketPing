package com.ticketping.gateway.presentation.response;

import org.springframework.http.HttpStatus;

public record CustomErrorResponse(HttpStatus status, String message) {
}
