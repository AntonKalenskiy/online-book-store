package com.springframework.boot.onlinebookstore.exception;

public class StatusNotExistException extends RuntimeException {
    public StatusNotExistException(String message) {
        super(message);
    }
}
