package com.ema.secondbrain.exception;

public class MalformedJwtException extends RuntimeException {
    public MalformedJwtException(String message) {
        super(message);
    }
}
