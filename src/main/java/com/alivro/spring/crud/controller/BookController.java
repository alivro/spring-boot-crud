package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.handler.ResponseHandler;
import com.alivro.spring.crud.model.book.request.BookSaveRequestDto;
import com.alivro.spring.crud.model.book.response.BookResponseDto;
import com.alivro.spring.crud.service.IBookService;
import com.alivro.spring.crud.util.CustomData;
import com.alivro.spring.crud.util.CustomPageMetadata;
import com.alivro.spring.crud.util.CustomResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book")
@CrossOrigin(origins = "http://localhost:8080")
public class BookController {
    private final IBookService bookService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * Constructor
     *
     * @param bookService Book service
     */
    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Endpoint para buscar todos los libros
     *
     * @return Información de todos los libros
     */
    @GetMapping("/findAll")
    public ResponseEntity<CustomResponse<BookResponseDto, CustomPageMetadata>> findAllBooks(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomData<BookResponseDto, CustomPageMetadata> booksData = bookService.findAll(pageable);

        logger.info("Libros encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found books!", booksData.getData(), booksData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar un libro por su ID
     *
     * @param id Identificador único del libro
     * @return Información del libro buscado
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<CustomResponse<BookResponseDto, Void>> findBook(@PathVariable("id") long id) {
        BookResponseDto foundBook = bookService.findById(id);

        logger.info("Libro encontrado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found book!", foundBook
        );
    }

    /**
     * Endpoint para guardar un nuevo libro
     *
     * @param book Información del libro a guardar
     * @return Información del libro guardado
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<BookResponseDto, Void>> saveBook(
            @Valid @RequestBody BookSaveRequestDto book) {
        BookResponseDto savedBook = bookService.save(book);

        logger.info("Libro guardado. ID: {}", savedBook.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved book!", savedBook
        );
    }

    /**
     * Endpoint para actualizar la información de un libro
     *
     * @param id   Identificador único del libro
     * @param book Información del libro a actualizar
     * @return Información del libro actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<BookResponseDto, Void>> updateBook(
            @PathVariable("id") long id, @Valid @RequestBody BookSaveRequestDto book) {
        BookResponseDto updatedBook = bookService.update(id, book);

        logger.info("Libro actualizado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated book!", updatedBook
        );
    }

    /**
     * Endpoint para eliminar un libro por su ID
     *
     * @param id El identificador único del libro
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<BookResponseDto, Void>> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);

        logger.info("Libro eliminado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted book!"
        );
    }
}
