package com.alivro.spring.crud.exception;

public class DataAlreadyExistsException extends RuntimeException {
    public DataAlreadyExistsException(String message){
        super(message);
    }
}
