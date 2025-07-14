package com.duoc.msvc.pedido.exceptions;

import com.duoc.msvc.pedido.dtos.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorDTO createErrorDTO(int status, Date date, Map<String, String> errorMap) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus(status);
        errorDTO.setDate(date);
        errorDTO.setErrors(errorMap);
        return errorDTO;
    }

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<ErrorDTO> handlePedidoException(PedidoException ex) {
        logger.error("GlobalExceptionHandler - PedidoException: {}", ex.getMessage());
        Map<String, String> errorMap = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("GlobalExceptionHandler - EntityNotFoundException: {}", ex.getMessage());
        Map<String, String> errorMap = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorDTO(HttpStatus.NOT_FOUND.value(), new Date(), errorMap));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.error("GlobalExceptionHandler - ValidationException: {}", ex.getMessage());
        Map<String, String> errorMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolation(ConstraintViolationException ex) {
        logger.error("GlobalExceptionHandler - ConstraintViolationException: {}", ex.getMessage());
        Map<String, String> errorMap = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(v -> v.getPropertyPath().toString(), v -> v.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex) {
        logger.error("GlobalExceptionHandler - GlobalException: {}", ex.getMessage(), ex);
        Map<String, String> errorMap = Map.of("error", "Error interno del servidor: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(), errorMap));
    }
}