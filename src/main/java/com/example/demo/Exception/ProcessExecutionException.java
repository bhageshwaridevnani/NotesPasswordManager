package com.example.demo.Exception;


public abstract class ProcessExecutionException extends RuntimeException {

    ProcessExecutionException() {
        super();
    }

    ProcessExecutionException(String message) {
        super(message);
    }

    public ProcessExecutionException(Throwable ex) {
        super(ex);
    }

}
