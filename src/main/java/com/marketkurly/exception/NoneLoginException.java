package com.marketkurly.exception;

public class NoneLoginException extends IllegalStateException {
    public NoneLoginException(String message) {
        super(message);
    }
}
