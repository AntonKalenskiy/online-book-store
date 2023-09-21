package com.springframework.boot.onlinebookstore.exception;

public class ShoppingCartNotExistOrEmptyException extends RuntimeException {
    public ShoppingCartNotExistOrEmptyException(String message) {
        super(message);
    }
}
