package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@ControllerAdvice
public class ErrHandler {

    @ExceptionHandler(NoSuchBucketException.class)
    public ResponseEntity<?> handleNoSuchBucketException(NoSuchBucketException e) {
        return ResponseEntity.badRequest().body(
                new AppError(
                        400,
                        "File does not exist"
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(
                new AppError(
                        500,
                        e.getMessage()
                )
        );
    }
}
