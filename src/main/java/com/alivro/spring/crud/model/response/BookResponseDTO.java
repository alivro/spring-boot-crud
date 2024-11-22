package com.alivro.spring.crud.model.response;

import com.alivro.spring.crud.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    // Identificador único del libro
    private long id;

    // Titulo del libro
    private String title;

    // Autor del libro
    private String author;

    // Número total de páginas
    private int totalPages;

    // Editorial que publicó libro
    private String publisher;

    // Fecha de publicación del libro
    private LocalDate publishedDate;

    // Identificador internacional único del libro
    private String isbn;

    public static BookResponseDTO entityToResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalPages(book.getTotalPages())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .build();
    }
}
