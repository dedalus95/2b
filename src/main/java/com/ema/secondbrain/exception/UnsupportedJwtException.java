package com.ema.secondbrain.exception;

public class UnsupportedJwtException extends RuntimeException {
    public UnsupportedJwtException(String message) {
        super(message);
    }
}
