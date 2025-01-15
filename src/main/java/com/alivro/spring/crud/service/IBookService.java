package com.alivro.spring.crud.service;

import com.alivro.spring.crud.model.book.request.BookSaveRequestDto;
import com.alivro.spring.crud.model.book.response.BookResponseDto;
import com.alivro.spring.crud.util.CustomData;
import com.alivro.spring.crud.util.PageMetadata;
import org.springframework.data.domain.Pageable;

public interface IBookService {
    /**
     * Método para buscar todos los libros
     *
     * @return Información de todos los libros
     */
    CustomData<BookResponseDto, PageMetadata> findAll(Pageable pageable);

    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return Información del libro buscado
     */
    BookResponseDto findById(Long id);

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Información del libro a guardar
     * @return Información del libro guardado
     */
    BookResponseDto save(BookSaveRequestDto book);

    /**
     * Método para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Información del libro a actualizar
     * @return Información del libro actualizado
     */
    BookResponseDto update(Long id, BookSaveRequestDto book);

    /**
     * Método para eliminar un libro por su ID
     *
     * @param id Identificador único del libro
     */
    void deleteById(Long id);
}
