package com.alivro.spring.crud.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    // Código de estado HTTP
    private int status;
    // Mensaje de error
    private String error;
    // URL de la solicitud
    private String path;
    // Marca de tiempo
    private Timestamp timestamp;
}
