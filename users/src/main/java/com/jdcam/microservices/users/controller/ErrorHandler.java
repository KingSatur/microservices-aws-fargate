package com.jdcam.microservices.users.controller;

import com.jdcam.microservices.users.exception.AlreadyExistsEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = { AlreadyExistsEmailException.class})
    public ResponseEntity<?> handleAlreadyExistEmail(
            AlreadyExistsEmailException exception
    ){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(
            ConstraintViolationException exception
    ){
        return handleValidationsErrors(exception.getConstraintViolations());
    }



    private static ResponseEntity<Map<String, String>> handleValidationsErrors(Set<ConstraintViolation<?>> violationSet) {
        Map<String, String> errors = new HashMap<>();
        violationSet.stream().forEach(err -> {
            errors.put(err.getPropertyPath().toString(), err.getMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}


