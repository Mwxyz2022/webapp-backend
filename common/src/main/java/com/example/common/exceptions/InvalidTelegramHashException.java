package com.example.common.exceptions;

public class InvalidTelegramHashException extends BaseException {

    public InvalidTelegramHashException() {
        super("Invalid Telegram hash", 401);
    }
}
