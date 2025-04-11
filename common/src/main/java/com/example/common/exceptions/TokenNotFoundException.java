package com.example.common.exceptions;

public class TokenNotFoundException extends BaseException {

    public TokenNotFoundException() {
        super("Token not found or invalidated", 401);
    }
}
