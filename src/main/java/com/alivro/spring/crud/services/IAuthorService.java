package com.alivro.spring.crud.services;

import com.alivro.spring.crud.model.request.AuthorRequestDto;
import com.alivro.spring.crud.model.response.AuthorResponseDto;

import java.util.List;

public interface IAuthorService {
    /**
     * Método para buscar todos los autores
     *
     * @return Información de todos los autores
     */
    List<AuthorResponseDto> findAll();

    /**
     * Método para buscar un autor por su ID
     *
     * @param id Identificador único del autor
     * @return Información del autor buscado
     */
    AuthorResponseDto findById(Long id);

    /**
     * Método para guardar un nuevo autor
     *
     * @param author Información del autor a guardar
     * @return Información del autor guardado
     */
    AuthorResponseDto save(AuthorRequestDto author);

    /**
     * Método para actualizar la información de un autor
     *
     * @param id     Identificador único del autor
     * @param author Información del autor a actualizar
     * @return Información del autor actualizado
     */
    AuthorResponseDto update(Long id, AuthorRequestDto author);

    /**
     * Método para eliminar un autor por su ID
     *
     * @param id Identificador único del autor
     */
    void deleteById(Long id);
}
