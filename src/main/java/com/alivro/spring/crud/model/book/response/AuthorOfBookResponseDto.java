package com.alivro.spring.crud.model.book.response;

import com.alivro.spring.crud.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorOfBookResponseDto {
    // Identificador único del autor
    private long id;

    // Pseudónimo único del autor
    private String pseudonym;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param author Información del autor
     * @return Representación ResponseDto de la información del autor
     */
    public static AuthorOfBookResponseDto mapEntityToResponseDto(Author author) {
        return AuthorOfBookResponseDto.builder()
                .id(author.getId())
                .pseudonym(author.getPseudonym())
                .build();
    }
}
