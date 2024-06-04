package com.phucx.blogapi.controller;

import javax.naming.NameNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.blogapi.exception.UserAuthenticationException;
import com.phucx.blogapi.model.ResponseError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{
    
    @ExceptionHandler(value = UserAuthenticationException.class)
    protected ResponseEntity<ResponseError> handleUserAuthenticationException(Exception exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ResponseError(exception.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<ResponseError> handleRuntimeException(Exception exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ResponseError(exception.getMessage()));
    }

    @ExceptionHandler(value = NameNotFoundException.class)
    protected ResponseEntity<ResponseError> handleNameNotFoundException(Exception exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ResponseError(exception.getMessage()));
    }
}
