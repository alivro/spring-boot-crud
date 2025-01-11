package com.alivro.spring.crud.handler;

import com.alivro.spring.crud.util.CustomErrorResponse;
import com.alivro.spring.crud.util.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class ResponseHandler {
    private ResponseHandler() {
    }

    /**
     * Método para enviar una respuesta
     *
     * @param status  Código de estado HTTP
     * @param message Mensaje
     * @param data    Lista de objetos
     * @return Respuesta HTTP
     */
    public static <T> ResponseEntity<CustomResponse<T>> sendResponse(
            HttpStatus status, String message, List<T> data) {
        CustomResponse<T> response = CustomResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * Método para enviar una respuesta
     *
     * @param status  Código de estado HTTP
     * @param message Mensaje
     * @param data    Objeto
     * @return Respuesta HTTP
     */
    public static <T> ResponseEntity<CustomResponse<T>> sendResponse(
            HttpStatus status, String message, T data) {
        return sendResponse(status, message, Collections.singletonList(data));
    }

    /**
     * Método para enviar una respuesta de error
     *
     * @param status Código de estado HTTP
     * @param error Mensaje de error
     * @param path   URL de la solicitud
     * @return Respuesta HTTP
     */
    public static ResponseEntity<CustomErrorResponse> sendErrorResponse(
            HttpStatus status, String error, String path) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(status.value())
                .error(error)
                .path(path)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        return new ResponseEntity<>(response, status);
    }
}