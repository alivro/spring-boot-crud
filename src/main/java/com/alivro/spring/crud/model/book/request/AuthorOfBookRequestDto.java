package com.alivro.spring.crud.model.book.request;

import com.alivro.spring.crud.model.Author;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorOfBookRequestDto {
    @Positive(message = "El campo id debe ser un número positivo.")
    private long id;

    @Size(min = 1, max = 100, message = "El campo pseudónimo debe tener como máximo 100 caracteres.")
    private String pseudonym;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param author Información del autor
     * @return Representación Entity de la información del autor
     */
    public static Author mapRequestDtoToEntity(AuthorOfBookRequestDto author) {
        return Author.builder()
                .id(author.getId())
                .pseudonym(author.getPseudonym())
                .build();
    }
}
