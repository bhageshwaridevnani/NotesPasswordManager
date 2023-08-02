package com.example.demo.Controller;


import com.example.demo.Base.ApiResponse;
import com.example.demo.Service.MessageService;
import com.example.demo.Util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;

    protected <T> ResponseEntity<?> okSuccessResponse(T t, String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(t, messageService.getMessage(messageId), Constant.SUCCESS), HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> okWarningResponse(T t, String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(t, messageService.getMessage(messageId), Constant.WARN), HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> okFailResponse(T t, String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(t, messageService.getMessage(messageId), Constant.FAIL), HttpStatus.OK);
    }

    protected ResponseEntity okSuccessResponse(String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), messageService.getMessage(messageId), Constant.SUCCESS), HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> okWarningResponse( String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), messageService.getMessage(messageId), Constant.WARN), HttpStatus.OK);
    }


    protected <T> ResponseEntity<?> okSuccessResponse(T t, String messageId, String... params) {
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), messageService.getMessage(messageId,params), Constant.SUCCESS), HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> okSuccessResponseResource(T t, String messageId, String... params) {
        return new ResponseEntity<>(new ApiResponse<>(t, messageService.getMessage(messageId,params), Constant.SUCCESS), HttpStatus.OK);
    }

    protected <T> ResponseEntity<?> okWarningResponse(T t, String messageId, String... params) {
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), messageService.getMessage(messageId,params), Constant.WARN), HttpStatus.OK);
    }


    protected ResponseEntity okFailResponse(String messageId) {
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), messageService.getMessage(messageId), Constant.FAIL), HttpStatus.OK);
    }



}
