package com.github.paulosalonso.notifier.adapter.notifier.email.common;

public class EmailException extends RuntimeException {
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
