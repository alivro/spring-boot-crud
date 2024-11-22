package com.alivro.spring.crud.services.impl;

import com.alivro.spring.crud.model.response.BookResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.request.BookRequestDTO;
import com.alivro.spring.crud.repository.BookRepository;
import com.alivro.spring.crud.services.IBookService;

@Service
public class IBookServiceImpl implements IBookService {
    private final BookRepository bookRepository;
    private final Logger logger = LoggerFactory.getLogger(IBookServiceImpl.class);

    /**
     * Constructor
     *
     * @param bookRepository
     */
    @Autowired
    public IBookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return El libro buscado
     */
    @Override
    public BookResponseDTO findById(Long id) {
        logger.info("Búsqueda de libro con ID: {}", id);
        Optional<Book> foundBook = bookRepository.findById(id);

        return foundBook.map(BookResponseDTO::entityToResponseDTO).orElse(null);
    }

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Datos del libro
     * @return El libro guardado
     */
    @Override
    public BookResponseDTO save(BookRequestDTO book) {
        logger.info("Búsqueda de libro con ISBN: {}", book.getIsbn());

        // Verifica si ya existe un libro con el mismo ISBN
        if (bookRepository.existsByIsbn(book.getIsbn()))
            return null;

        logger.info("Guardado de libro con ISBN: {}", book.getIsbn());

        // Guarda el nuevo libro
        return BookResponseDTO.entityToResponseDTO(
                bookRepository.save(
                        BookRequestDTO.requestDTOtoEntity(book)
                )
        );
    }

    /**
     * Método para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Datos del libro
     * @return El libro actualizado
     */
    @Override
    public BookResponseDTO updateById(Long id, BookRequestDTO book) {
        logger.info("Búsqueda de libro con id: {}", id);

        // Verifica si existe un libro con ese id
        if (!bookRepository.existsById(id))
            return null;

        logger.info("Actualización de libro con id: {}", id);

        // Actualiza la informaciṕn del libro
        return BookResponseDTO.entityToResponseDTO(
                bookRepository.save(
                        BookRequestDTO.requestDTOtoEntity(id, book)
                )
        );
    }

    /**
     * Método para eliminar un libro por su ID
     *
     * @param id Identificador único del libro
     */
    @Override
    public void deleteById(Long id) {
        logger.info("Eliminación de libro con ID: {}", id);
        bookRepository.deleteById(id);
    }
}
