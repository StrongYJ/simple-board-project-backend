package com.myproject.simpleboard.global.error;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;

@RestControllerAdvice("com.myproject.simpleboard")
public class ErrorControllerAdvice {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> methodArgumentNotValidEx(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("path", request.getRequestURI());
        map.put("status", HttpStatus.BAD_REQUEST);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        map.put("exceptions", fieldErrors.stream().map(error -> 
            new FieldException(error.getField(), error.getDefaultMessage(), error.getRejectedValue())
        ));
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    public ErrorResult unexpectedTypeEx(UnexpectedTypeException e) {
        return new ErrorResult(HttpStatus.BAD_REQUEST, e.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult noSuchElementEx(IllegalArgumentException e) {
        return new ErrorResult(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult noSuchElementEx(NoSuchElementException e) {
        return new ErrorResult(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(DisabledException.class)
    public ErrorResult disabledEx(DisabledException e) {
        return new ErrorResult(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResult accessDeniedEx(AccessDeniedException e) {
        return new ErrorResult(HttpStatus.FORBIDDEN, e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResult runtimeEx(RuntimeException e) {
        return new ErrorResult(HttpStatus.BAD_GATEWAY, e.getMessage());
    }


    private record FieldException(String field, String message, Object rejectedValue) {
        FieldException {
            rejectedValue = rejectedValue == null ? null : rejectedValue.toString();
        }
    }
}
