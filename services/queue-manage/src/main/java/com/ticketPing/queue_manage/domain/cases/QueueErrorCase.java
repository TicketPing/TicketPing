package com.ticketPing.queue_manage.domain.cases;

import cases.ErrorCase;
import org.springframework.http.HttpStatus;

public interface QueueErrorCase extends ErrorCase {
    HttpStatus getHttpStatus();
    String getMessage();
}

