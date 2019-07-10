package com.feng.pigp.fans.exception;

/**
 * @author feng
 * @date 2019/7/10 11:36
 * @since 1.0
 */
public class FansException extends RuntimeException {

    private String message;

    public FansException(String message) {
        super(message);
        this.message = message;
    }
}