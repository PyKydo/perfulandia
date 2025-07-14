package com.duoc.msvc.pago.exceptions;

import com.duoc.msvc.pago.dtos.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("GlobalExceptionHandler - ValidationException: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
            errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PagoException.class)
    public ResponseEntity<ErrorDTO> handlePagoException(PagoException ex) {
        logger.error("GlobalExceptionHandler - PagoException: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("GlobalExceptionHandler - EntityNotFoundException: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error("GlobalExceptionHandler - ConstraintViolationException: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex) {
        logger.error("GlobalExceptionHandler - GlobalException: {}", ex.getMessage(), ex);
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Error interno del servidor: " + ex.getMessage());
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

