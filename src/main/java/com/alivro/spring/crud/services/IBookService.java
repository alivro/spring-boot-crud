package com.alivro.spring.crud.services;

import com.alivro.spring.crud.model.request.BookRequestDTO;
import com.alivro.spring.crud.model.response.BookResponseDTO;

public interface IBookService {
    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return El libro buscado
     */
    public BookResponseDTO findById(Long id);

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Datos del libro
     * @return El libro guardado
     */
    public BookResponseDTO save(BookRequestDTO book);

    /**
     * Método para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Datos del libro
     * @return El libro actualizado
     */
    public BookResponseDTO updateById(Long id, BookRequestDTO book);

    /**
     * Método para eliminar un libro por su ID
     *
     * @param id Identificador único del libro
     */
    public void deleteById(Long id);
}
