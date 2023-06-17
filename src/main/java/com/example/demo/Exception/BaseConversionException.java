package com.example.demo.Exception;

public class BaseConversionException extends ProcessExecutionException {

    public BaseConversionException(String message) {
        super(message);
    }

    public BaseConversionException(Exception e) {
        super(e);
    }
}
