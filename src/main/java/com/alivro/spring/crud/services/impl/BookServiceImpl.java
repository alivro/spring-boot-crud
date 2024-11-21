package com.alivro.spring.crud.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.request.BookRequest;
import com.alivro.spring.crud.repository.BookRepository;
import com.alivro.spring.crud.services.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    /**
     * Constructor
     *
     * @param bookRepository
     */
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

     /**
     * Método para buscar un libro por su ID
     *
     * @param   id      Identificador único del libro
     * @return          Información del libro buscado
     */
    @Override
    public Optional<Book> findById(Long id) {
        logger.info("Búsqueda de libro con ID: {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Método para guardar un nuevo libro
     *
     * @param book      Datos del libro
     * @return          Información del libro guardado
     */
    @Override
    public Book save(BookRequest book) {
        // Verifica si ya existe un libro con el mismo ISBN
        if(existsByIsbn(book.getIsbn())) {
            logger.info("Ya existe un libro con el ISBN: {}", book.getIsbn());
            return null;
        }

        // Guarda el nuevo libro
        return bookRepository.save(
            Book.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalPages(book.getTotalPages())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
            .build()
        );
    }

    /**
     * Método para actualizar la información de un libro
     *
     * @param   id      Identificador único del libro
     * @param book      Datos del libro
     * @return          Información del libro actualizado
     */
    @Override
    public Book updateById(Long id, BookRequest book){
        Optional<Book> findBook = bookRepository.findById(id);

        // Verifica si existe un libro con ese ID
        if(findBook.isEmpty())
            return null;

        // Actualiza la información del libro
        return bookRepository.save(
            Book.builder()
                .id(id)
                .title(book.getTitle())
                .author(book.getAuthor())
                .totalPages(book.getTotalPages())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .isbn(book.getIsbn())
                .build()
        );
    }

    /**
     * Método para eliminar un libro por su ID
     *
     * @param   id      El identificador único del libro
     * @return          No devuelve nigún valor
     */
    @Override
    public void deleteById(Long id) {
        logger.info("Eliminación de libro con ID: {}", id);
        bookRepository.deleteById(id);
    }

    /**
     * Método para buscar la existencia de un libro por su ISBN
     *
     * @param   isbn    Identificador único del libro
     * @return          true si existe, en caso contrario, false
     */
    private boolean existsByIsbn(String isbn) {
        logger.info("Búsqueda de libro con ISBN: {}", isbn);
        return bookRepository.existsByIsbn(isbn);
    }
}
