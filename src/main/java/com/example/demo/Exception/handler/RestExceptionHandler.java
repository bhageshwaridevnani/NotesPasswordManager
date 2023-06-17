package com.example.demo.Exception.handler;


import com.example.demo.Base.ApiResponse;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Exception.CommonInternalServerException;
import com.example.demo.Service.MessageService;
import com.example.demo.Util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.*;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);
    static final String INTERNAL_SERVER_ERROR_MSG = "Unable to perform given operation, please try after sometime.";
    @Autowired
    protected MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ex.printStackTrace();
        return buildResponseEntity(HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler({BusinessValidationException.class})
    protected ResponseEntity<Object> handleBusinessValidation(BusinessValidationException ex) {
        return buildResponseEntity(HttpStatus.OK, getMessage(ex.getMessage(), ex.getParams()));
    }

    @ExceptionHandler({CommonInternalServerException.class})
    public ResponseEntity<Object> handleInternalServiceError(CommonInternalServerException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);

    }
    private ResponseEntity<Object> buildResponseEntity(HttpStatus badRequest, String errorMessage) {
        LOG.error("apiError " + errorMessage);
        return new ResponseEntity<>(new ApiResponse<>(objectMapper.createObjectNode(), errorMessage, Constant.FAIL), badRequest);
    }
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
        LOG.error("---------------------Internal server Error------------------------");
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler({InvalidParameterException.class})
    protected ResponseEntity<Object> handleInvalidParameter(InvalidParameterException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required'
     * request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
//        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error));
        return buildResponseEntity(HttpStatus.BAD_REQUEST, error);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
     * invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
     * validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
     *                validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        return buildResponseEntity(HttpStatus.OK, getMessage(result.getFieldError().getDefaultMessage()));
    }

    private String getMessage(String key) {
        try {
            return messageService.getMessage(key);
        } catch (Exception e) {
            return key;
        }
    }

    private String getMessage(String key, Object[] params) {
        try {
            return messageService.getMessage(key, params);
        } catch (Exception e) {
            return key;
        }
    }
}
