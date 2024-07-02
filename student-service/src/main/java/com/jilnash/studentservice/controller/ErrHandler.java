package com.jilnash.studentservice.controller;

import com.jilnash.studentservice.dto.AppError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                new AppError(
                        400,
                        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                )
        );
    }
}
