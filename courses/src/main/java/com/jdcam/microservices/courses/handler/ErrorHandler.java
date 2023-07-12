package com.jdcam.microservices.courses.handler;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = { FeignException.class})
    public ResponseEntity<?> handleAlreadyExistEmail(
            FeignException exception
    ){
        return ResponseEntity.internalServerError().body("There was an error: " + exception.getMessage());
    }

}
