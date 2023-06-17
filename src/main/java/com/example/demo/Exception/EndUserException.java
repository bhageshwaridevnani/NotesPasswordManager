package com.example.demo.Exception;


public abstract class EndUserException extends RuntimeException {

    public EndUserException(String msg) {
        super(msg);
    }

    public EndUserException(Throwable t) {
        super(t);
    }
}
