package com.alivro.spring.crud.service.impl;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.book.request.BookSaveRequestDto;
import com.alivro.spring.crud.model.book.response.BookResponseDto;
import com.alivro.spring.crud.repository.BookRepository;
import com.alivro.spring.crud.service.IBookService;
import com.alivro.spring.crud.util.CustomData;
import com.alivro.spring.crud.util.PageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * Método para buscar todos los libros
     *
     * @return Información de todos los libros
     */
    @Override
    public CustomData<BookResponseDto, PageMetadata> findAll(Pageable pageable) {
        logger.info("Busca todos los libros.");

        Page<Book> booksPage = bookRepository.findAll(pageable);

        // Información de los libros
        List<BookResponseDto> foundBooks = booksPage.stream()
                .map(BookResponseDto::mapEntityToResponseDto)
                .toList();

        // Metadatos
        PageMetadata metadata = PageMetadata.builder()
                .pageNumber(booksPage.getNumber())
                .pageSize(booksPage.getSize())
                .numberOfElements(booksPage.getNumberOfElements())
                .totalPages(booksPage.getTotalPages())
                .totalElements(booksPage.getTotalElements())
                .build();

        return CustomData.<BookResponseDto, PageMetadata>builder()
                .data(foundBooks)
                .metadata(metadata)
                .build();
    }

    /**
     * Método para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return Información del libro buscado
     */
    @Override
    public BookResponseDto findById(Long id) {
        logger.info("Busca libro. ID: {}", id);

        Optional<Book> foundBook = bookRepository.findById(id);

        if (foundBook.isEmpty()) {
            logger.info("Libro no encontrado. ID: {}", id);

            throw new DataNotFoundException("Book not found!");
        }

        return BookResponseDto.mapEntityToResponseDto(foundBook.get());
    }

    /**
     * Método para guardar un nuevo libro
     *
     * @param book Información del libro a guardar
     * @return Información del libro guardado
     */
    @Override
    public BookResponseDto save(BookSaveRequestDto book) {
        String isbn13 = book.getIsbn13();

        logger.info("Busca libro. ISBN-13: {}", isbn13);

        // Verifica si ya existe un libro con el mismo ISBN-13
        if (bookRepository.existsByIsbn13(isbn13)) {
            logger.info("Libro existente. ISBN-13: {}", isbn13);
            logger.info("Libro no guardado. ISBN-13: {}", isbn13);

            throw new DataAlreadyExistsException("Book already exists!");
        }

        logger.info("Libro no existente. ISBN-13: {}", isbn13);
        logger.info("Guarda libro. ISBN-13: {}", isbn13);

        // Guarda la información del nuevo libro
        Book savedBook = bookRepository.save(
                BookSaveRequestDto.mapRequestDtoToEntity(book)
        );

        return BookResponseDto.mapEntityToResponseDto(savedBook);
    }

    /**
     * Método para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Información del libro a actualizar
     * @return Información del libro actualizado
     */
    @Override
    public BookResponseDto update(Long id, BookSaveRequestDto book) {
        logger.info("Busca libro. ID: {}", id);

        Optional<Book> foundBook = bookRepository.findById(id);

        // Verifica si existe un libro con ese id
        if (foundBook.isEmpty()) {
            logger.info("Libro no existente. ID: {}", id);
            logger.info("Libro no actualizado. ID: {}", id);

            throw new DataNotFoundException("Book does not exist!");
        }

        // Información del libro a actualizar
        Book bookToUpdate = foundBook.get();
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setSubtitle(book.getSubtitle());
        bookToUpdate.setTotalPages(book.getTotalPages());
        bookToUpdate.setPublisher(book.getPublisher());
        bookToUpdate.setPublishedDate(book.getPublishedDate());
        bookToUpdate.setIsbn13(book.getIsbn13());
        bookToUpdate.setIsbn10(book.getIsbn10());

        logger.info("Actualiza libro. ID: {}", id);

        // Actualiza la información del libro
        Book updatedBook = bookRepository.save(
                BookSaveRequestDto.mapRequestDtoToEntity(id, book)
        );

        return BookResponseDto.mapEntityToResponseDto(updatedBook);
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
