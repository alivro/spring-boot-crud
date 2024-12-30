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

    // Título del libro
    private String title;

    // Subtítulo del libro
    private String subtitle;

    // Autor del libro
    private String author;

    // Número total de páginas
    private int totalPages;

    // Editorial que publicó libro
    private String publisher;

    // Fecha de publicación del libro
    private LocalDate publishedDate;

    // Identificador internacional único del libro (13 dígitos)
    private String isbn13;

    // Identificador internacional único del libro (10 dígitos)
    private String isbn10;

    /**
     * Convierte un objeto Entity en un objeto ResponseDTO
     *
     * @param book Información del libro
     * @return Representación ResponseDTO de la información del libro
     */
    public static BookResponseDTO entityToResponseDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .author(book.getAuthor())
                .totalPages(book.getTotalPages())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn13(book.getIsbn13())
                .isbn10(book.getIsbn10())
                .build();
    }
}
