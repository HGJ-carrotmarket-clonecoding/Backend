package com.marketkurly.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
