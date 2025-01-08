package com.alivro.spring.crud.service;

import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;

import java.util.List;

public interface IAuthorService {
    /**
     * Método para buscar todos los autores
     *
     * @return Información de todos los autores
     */
    List<AuthorFindResponseDto> findAll();

    /**
     * Método para buscar un autor por su ID
     *
     * @param id Identificador único del autor
     * @return Información del autor buscado
     */
    AuthorFindResponseDto findById(Long id);

    /**
     * Método para guardar un nuevo autor
     *
     * @param author Información del autor a guardar
     * @return Información del autor guardado
     */
    AuthorSaveResponseDto save(AuthorSaveRequestDto author);

    /**
     * Método para actualizar la información de un autor
     *
     * @param id     Identificador único del autor
     * @param author Información del autor a actualizar
     * @return Información del autor actualizado
     */
    AuthorSaveResponseDto update(Long id, AuthorSaveRequestDto author);

    /**
     * Método para eliminar un autor por su ID
     *
     * @param id Identificador único del autor
     */
    void deleteById(Long id);
}
