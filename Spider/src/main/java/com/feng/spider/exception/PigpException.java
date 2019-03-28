package com.feng.spider.exception;

/**
 * @author feng
 * @date 2019/3/12 11:22
 * @since 1.0
 */
public class PigpException extends RuntimeException{

    private String message;

    public PigpException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}