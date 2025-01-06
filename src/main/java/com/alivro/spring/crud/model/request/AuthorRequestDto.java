package com.alivro.spring.crud.model.request;

import com.alivro.spring.crud.model.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequestDto {
    @NotBlank(message = "El campo primer nombre es obligatorio.")
    @Size(min = 1, max = 100, message = "El campo primer nombre debe tener como máximo 100 caracteres.")
    private String firstName;

    @Size(min = 1, max = 100, message = "El campo segundo nombre debe tener como máximo 100 caracteres.")
    private String middleName;

    @NotBlank(message = "El campo apellido es obligatorio.")
    @Size(min = 1, max = 100, message = "El campo apellido debe tener como máximo 100 caracteres.")
    private String lastName;

    @NotBlank(message = "El campo pseudónimo es obligatorio.")
    @Size(min = 1, max = 100, message = "El campo pseudónimo debe tener como máximo 100 caracteres.")
    private String pseudonym;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param author Información del autor
     * @return Representación Entity de la información del autor
     */
    public static Author mapRequestDtoToEntity(AuthorRequestDto author) {
        return Author.builder()
                .firstName(author.getFirstName())
                .middleName(author.getMiddleName())
                .lastName(author.getLastName())
                .pseudonym(author.getPseudonym())
                .build();
    }

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param id     Identificador único del autor
     * @param author Información del autor
     * @return Representación Entity de la información del autor
     */
    public static Author mapRequestDtoToEntity(long id, AuthorRequestDto author) {
        return mapRequestDtoToEntity(author)
                .toBuilder()
                .id(id)
                .build();
    }
}
