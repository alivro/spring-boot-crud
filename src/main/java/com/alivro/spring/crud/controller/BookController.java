package com.alivro.spring.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;

import com.alivro.spring.crud.services.BookService;
import com.alivro.spring.crud.model.request.BookRequest;
import com.alivro.spring.crud.model.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/book")
@CrossOrigin(origins = "http://localhost:8080")
public class BookController {
    private final BookService bookService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * Constructor
     *
     * @param bookService
     */
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Endpoint para buscar un libro por su ID
     *
     * @param   id      Identificador único del libro
     * @return          Información del libro buscado
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<Book> findBook(@PathVariable("id") long id) {
        Optional<Book> book = bookService.findById(id);

        if(book.isEmpty()) {
            logger.info("No se encontró ningún libro con el ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        logger.info("Libro encontrado");
        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    /**
     * Endpoint para guardar un nuevo libro
     *
     * @param book      Datos del libro
     * @return          Información del libro guardado
     */
    @PostMapping("/save")
    public ResponseEntity<Book> insertBook(@Valid @RequestBody BookRequest book) {
        Book newBook = bookService.save(book);

        if(newBook == null) {
            logger.info("Libro no guardado");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("Libro guardado correctamente");
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    /**
     * Endpoint para actualizar la información de un libro
     *
     * @param   id      Identificador único del libro
     * @param book      Datos del libro
     * @return          Información del libro actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @Valid @RequestBody BookRequest book) {
        Book updateBook = bookService.updateById(id, book);

        if(updateBook == null) {
            logger.info("No se encontró ningún libro con el ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Información del libro actualizada correctamente");
        return new ResponseEntity<>(updateBook, HttpStatus.OK);
    }

    /**
     * Endpoint para eliminar un libro por su ID
     *
     * @param   id      El identificador único del libro
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<Book>> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
