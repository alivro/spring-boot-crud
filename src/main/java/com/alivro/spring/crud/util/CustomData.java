package com.alivro.spring.crud.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomData<T, S> {
    // Lista de objetos
    private List<T> data;
    // Metadatos
    private S metadata;
}
