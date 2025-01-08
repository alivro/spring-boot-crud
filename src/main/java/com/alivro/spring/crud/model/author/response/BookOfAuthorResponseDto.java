package com.alivro.spring.crud.model.author.response;

import com.alivro.spring.crud.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOfAuthorResponseDto {
    // Identificador único del libro
    private long id;

    // Título del libro
    private String title;

    // Subtítulo del libro
    private String subtitle;

    // Editorial que publicó libro
    private String publisher;

    // Identificador internacional único del libro (13 dígitos)
    private String isbn13;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param book Información del libro
     * @return Representación ResponseDto de la información del libro
     */
    public static BookOfAuthorResponseDto mapEntityToResponseDto(Book book) {
        return BookOfAuthorResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .publisher(book.getPublisher())
                .isbn13(book.getIsbn13())
                .build();
    }
}
