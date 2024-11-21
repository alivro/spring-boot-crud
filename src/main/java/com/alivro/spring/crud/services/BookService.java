package com.alivro.spring.crud.services;

import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.request.BookRequest;

import java.util.Optional;

public interface BookService {
    /**
     * Método para buscar un libro por su ID
     *
     * @param   id      Identificador único del libro
     * @return          Información del libro buscado
     */
    public Optional<Book> findById(Long id);

    /**
     * Método para guardar un nuevo libro
     *
     * @param book      Datos del libro
     * @return          Información del libro guardado
     */
    public Book save(BookRequest book);

    /**
     * Método para actualizar la información de un libro
     *
     * @param   id      Identificador único del libro
     * @param book      Datos del libro
     * @return          Información del libro actualizado
     */
    public Book updateById(Long id, BookRequest book);

    /**
     * Método para eliminar un libro por su ID
     *
     * @param   id      El identificador único del libro
     * @return
     */
    public void deleteById(Long id);
}
