package com.alivro.spring.crud.model.book.response;

import com.alivro.spring.crud.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    // Identificador único del libro
    private long id;

    // Título del libro
    private String title;

    // Subtítulo del libro
    private String subtitle;

    // Autor(es) del libro
    private List<AuthorOfBookResponseDto> authors;

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
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param book Información del libro
     * @return Representación ResponseDto de la información del libro
     */
    public static BookResponseDto mapEntityToResponseDto(Book book) {
        List<AuthorOfBookResponseDto> authorsOfBook = book.getAuthors().stream()
                .map(AuthorOfBookResponseDto::mapEntityToResponseDto)
                .collect(Collectors.toList());

        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .authors(authorsOfBook)
                .totalPages(book.getTotalPages())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn13(book.getIsbn13())
                .isbn10(book.getIsbn10())
                .build();
    }
}
