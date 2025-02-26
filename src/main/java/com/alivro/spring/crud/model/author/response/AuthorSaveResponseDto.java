package com.alivro.spring.crud.model.author.response;

import com.alivro.spring.crud.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorSaveResponseDto {
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

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param author Información del autor
     * @return Representación ResponseDto de la información del autor
     */
    public static AuthorSaveResponseDto mapEntityToResponseDto(Author author) {
        return AuthorSaveResponseDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .middleName(author.getMiddleName())
                .lastName(author.getLastName())
                .pseudonym(author.getPseudonym())
                .build();
    }
}
