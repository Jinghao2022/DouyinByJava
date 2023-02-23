package com.bytedance.douyinbyjava.config;

public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
