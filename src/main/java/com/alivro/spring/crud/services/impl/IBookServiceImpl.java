package com.alivro.spring.crud.services.impl;

import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.request.BookRequestDTO;
import com.alivro.spring.crud.model.response.BookResponseDTO;
import com.alivro.spring.crud.repository.BookRepository;
import com.alivro.spring.crud.services.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IBookServiceImpl implements IBookService {
    private final BookRepository bookRepository;
    private final Logger logger = LoggerFactory.getLogger(IBookServiceImpl.class);

    /**
     * Constructor
     *
     * @param bookRepository Book repository
     */
    @Autowired
    public IBookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return Información del libro buscado
     */
    @Override
    public BookResponseDTO findById(Long id) {
        logger.info("Busca libro. ID: {}", id);
        Optional<Book> foundBook = bookRepository.findById(id);

        return foundBook.map(BookResponseDTO::entityToResponseDTO).orElse(null);
    }

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Información del libro a guardar
     * @return Información del libro guardado
     */
    @Override
    public BookResponseDTO save(BookRequestDTO book) {
        logger.info("Busca libro. ISBN-13: {}", book.getIsbn13());

        // Verifica si ya existe un libro con el mismo ISBN-13
        if (bookRepository.existsByIsbn13(book.getIsbn13())) {
            logger.info("Libro existente. ISBN-13: {}", book.getIsbn13());
            return null;
        }

        logger.info("Libro no existente. ISBN-13: {}", book.getIsbn13());
        logger.info("Guarda libro. ISBN-13: {}", book.getIsbn13());

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
     * @param book Información del libro a actualizar
     * @return Información del libro actualizado
     */
    @Override
    public BookResponseDTO updateById(Long id, BookRequestDTO book) {
        logger.info("Busca libro. ID: {}", id);

        // Verifica si existe un libro con ese id
        if (!bookRepository.existsById(id)) {
            logger.info("Libro no existente. ID: {}", id);
            return null;
        }

        logger.info("Actualiza libro. ID: {}", id);

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
        logger.info("Elimina libro. ID: {}", id);

        bookRepository.deleteById(id);
    }
}
