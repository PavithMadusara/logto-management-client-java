package com.aupma.logto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String message) {
        super(message);
        log.error(message);
    }
}
