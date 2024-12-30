package com.alivro.spring.crud.services;

import com.alivro.spring.crud.model.request.BookRequestDTO;
import com.alivro.spring.crud.model.response.BookResponseDTO;

public interface IBookService {
    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return Información del libro buscado
     */
    BookResponseDTO findById(Long id);

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Información del libro a guardar
     * @return Información del libro guardado
     */
    BookResponseDTO save(BookRequestDTO book);

    /**
     * Método para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Información del libro a actualizar
     * @return Información del libro actualizado
     */
    BookResponseDTO updateById(Long id, BookRequestDTO book);

    /**
     * Método para eliminar un libro por su ID
     *
     * @param id Identificador único del libro
     */
    void deleteById(Long id);
}
