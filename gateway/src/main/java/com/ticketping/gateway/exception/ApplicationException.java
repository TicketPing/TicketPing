package com.ticketping.gateway.exception;

import com.ticketping.gateway.presentation.cases.ErrorCase;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCase errorCase;

    public ApplicationException(ErrorCase errorCase) {
        super(errorCase.getMessage());
        this.errorCase = errorCase;
    }
}
