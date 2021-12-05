package com.ioc.exception;

public class NotFoundBeanException extends RuntimeException{
    public NotFoundBeanException() {
        super();
    }

    public NotFoundBeanException(String message) {
        super(message);
    }
}
