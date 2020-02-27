package com.kowalx.transactions.viewer.exception;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {

    private final int status;
    private final LocalDateTime timeStamp;
    private final String message;
}
