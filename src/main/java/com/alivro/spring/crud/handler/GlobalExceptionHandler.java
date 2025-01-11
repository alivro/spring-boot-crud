package com.alivro.spring.crud.handler;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.util.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleDataNotFoundException(
            DataNotFoundException ex, HttpServletRequest request) {
        return ResponseHandler.sendErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()
        );
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleDataAlreadyExistsException(
            DataAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseHandler.sendErrorResponse(
                HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        return ResponseHandler.sendErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI()
        );
    }
}
