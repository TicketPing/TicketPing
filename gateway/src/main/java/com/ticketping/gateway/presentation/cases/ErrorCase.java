package com.ticketping.gateway.presentation.cases;

import org.springframework.http.HttpStatus;

public interface ErrorCase {
    HttpStatus getHttpStatus();
    String getMessage();
}
