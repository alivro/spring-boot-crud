package com.alivro.spring.crud.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    @NotBlank(message = "El campo título es obligatorio.")
    private String title;

    @NotBlank(message = "El campo autor es obligatorio.")
    private String author;

    @Positive(message = "El campo total de páginas debe ser un número positivo.")
    private int totalPages;

    @NotBlank(message = "El campo editorial es obligatorio.")
    private String publisher;

    @NotNull(message = "El campo fecha de publicación es obligatorio.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publishedDate;

    @NotBlank(message = "El campo ISBN es obligatorio.")
    @Size(min = 13, max = 13, message = "El campo ISBN debe tener 13 caracteres.")
    private String isbn;
}
