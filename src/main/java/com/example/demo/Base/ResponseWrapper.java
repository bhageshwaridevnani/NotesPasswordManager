package com.example.demo.Base;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapper<T> {

    private T data;
    private String message;
    private int status;

}
