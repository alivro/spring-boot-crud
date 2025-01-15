package com.alivro.spring.crud.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageMetadata {
    // Número de página
    private int pageNumber;
    // Tamaño de la página
    private int pageSize;
    // Número de elementos en la página
    private int numberOfElements;
    // Número total de páginas
    private int totalPages;
    // Número total de elementos
    private long totalElements;
}
