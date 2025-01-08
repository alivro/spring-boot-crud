package com.alivro.spring.crud.model.author.response;

import com.alivro.spring.crud.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorFindResponseDto {
    // Identificador único del autor
    private long id;

    // Nombre del autor
    private String firstName;

    // Segundo nombre del autor
    private String middleName;

    // Apellido del autor
    private String lastName;

    // Pseudónimo único del autor
    private String pseudonym;

    // Libro(s) del autor
    private List<BookOfAuthorResponseDto> books;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param author Información del autor
     * @return Representación ResponseDto de la información del autor
     */
    public static AuthorFindResponseDto mapEntityToResponseDto(Author author) {
        List<BookOfAuthorResponseDto> booksOfAuthor = author.getBooks().stream()
                .map(BookOfAuthorResponseDto::mapEntityToResponseDto)
                .collect(Collectors.toList());

        return AuthorFindResponseDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .middleName(author.getMiddleName())
                .lastName(author.getLastName())
                .pseudonym(author.getPseudonym())
                .books(booksOfAuthor)
                .build();
    }
}
