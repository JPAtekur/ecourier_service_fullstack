package com.ecourier.trackingservice.exception;

import com.ecourier.trackingservice.dto.ApiError;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(new ApiError("Validation failed", errors));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ApiError(ex.getMessage(), null));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleFeignError(FeignException ex) {
        log.error("Error calling parcel service", ex);
        return ResponseEntity
                .status(ex.status())
                .body(new ApiError("Error communicating with parcel service", null));
    }
}