package com.nosify.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailNotVerifyException extends RuntimeException {
    public EmailNotVerifyException() {
    }

    public EmailNotVerifyException(String msg) {
        super(msg);
    }
}
