package com.feng.pigp.fans.exception;

public class AccountErrorException extends Error{

    private String message;

    public AccountErrorException(String message) {
        this.message = message;
    }
}