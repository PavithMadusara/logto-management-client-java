package com.aupma.logto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogtoApiException extends RuntimeException {
    public LogtoApiException(String message) {
        super(message);
        log.error(message);
    }
}
