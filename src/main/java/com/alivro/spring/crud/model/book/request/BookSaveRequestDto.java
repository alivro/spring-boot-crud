package com.alivro.spring.crud.model.book.request;

import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class BookSaveRequestDto {
    @NotBlank(message = "El campo título es obligatorio.")
    @Size(min = 1, max = 255, message = "El campo título debe tener como máximo 255 caracteres.")
    private String title;

    @Size(min = 1, max = 255, message = "El campo subtítulo debe tener como máximo 255 caracteres.")
    private String subtitle;

    @Valid
    @NotNull(message = "El campo autor(es) es obligatorio.")
    private List<AuthorOfBookRequestDto> authors;

    @Positive(message = "El campo total de páginas debe ser un número positivo.")
    private int totalPages;

    @NotBlank(message = "El campo editorial es obligatorio.")
    @Size(min = 1, max = 50, message = "El campo editorial debe tener como máximo 50 caracteres.")
    private String publisher;

    @NotNull(message = "El campo fecha de publicación es obligatorio.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    @NotBlank(message = "El campo ISBN-13 es obligatorio.")
    @Size(min = 13, max = 13, message = "El campo ISBN-13 debe tener 13 dígitos.")
    private String isbn13;

    @Size(min = 10, max = 10, message = "El campo ISBN-10 debe tener 10 dígitos.")
    private String isbn10;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param book Información del libro
     * @return Representación Entity de la información del libro
     */
    public static Book mapRequestDtoToEntity(BookSaveRequestDto book) {
        List<Author> authorsOfBook = book.getAuthors().stream()
                .map(AuthorOfBookRequestDto::mapRequestDtoToEntity)
                .collect(Collectors.toList());

        return Book.builder()
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

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param id   Identificador único del libro
     * @param book Información del libro
     * @return Representación Entity de la información del libro
     */
    public static Book mapRequestDtoToEntity(long id, BookSaveRequestDto book) {
        return mapRequestDtoToEntity(book)
                .toBuilder()
                .id(id)
                .build();
    }
}
