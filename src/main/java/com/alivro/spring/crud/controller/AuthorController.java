package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.handler.ResponseHandler;
import com.alivro.spring.crud.model.request.AuthorRequestDto;
import com.alivro.spring.crud.model.response.AuthorResponseDto;
import com.alivro.spring.crud.services.IAuthorService;
import com.alivro.spring.crud.util.CustomResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/author")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthorController {
    private final IAuthorService authorService;
    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    /**
     * Constructor
     *
     * @param authorService Author service
     */
    @Autowired
    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * Endpoint para buscar todos los autores
     *
     * @return Información de todos los autores
     */
    @GetMapping("/findAll")
    public ResponseEntity<CustomResponse<AuthorResponseDto>> findAllAuthors() {
        List<AuthorResponseDto> foundAuthors = authorService.findAll();

        logger.info("Autores encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found authors!", foundAuthors
        );
    }

    /**
     * Endpoint para buscar un autor por su ID
     *
     * @param id Identificador único del autor
     * @return Información del autor buscado
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<CustomResponse<AuthorResponseDto>> findAuthor(@PathVariable("id") long id) {
        AuthorResponseDto foundAuthor = authorService.findById(id);

        if (foundAuthor == null) {
            logger.info("Autor no encontrado. ID: {}", id);

            return ResponseHandler.sendResponse(
                    HttpStatus.NOT_FOUND, "Author not found!", null
            );
        }

        logger.info("Autor encontrado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found author!", foundAuthor
        );
    }

    /**
     * Endpoint para guardar un nuevo autor
     *
     * @param author Información del autor a guardar
     * @return Información del autor guardado
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<AuthorResponseDto>> saveAuthor(@Valid @RequestBody AuthorRequestDto author) {
        AuthorResponseDto savedAuthor = authorService.save(author);

        if (savedAuthor == null) {
            logger.info("Autor no guardado. Pseudónimo: {}", author.getPseudonym());

            return ResponseHandler.sendResponse(
                    HttpStatus.CONFLICT, "Author not saved!", null
            );
        }

        logger.info("Autor guardado. ID: {}", savedAuthor.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved author!", savedAuthor
        );
    }

    /**
     * Endpoint para actualizar la información de un autor
     *
     * @param id     Identificador único del autor
     * @param author Información del autor a actualizar
     * @return Información del autor actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<AuthorResponseDto>> updateAuthor(
            @PathVariable("id") long id, @Valid @RequestBody AuthorRequestDto author) {
        AuthorResponseDto updatedAuthor = authorService.update(id, author);

        if (updatedAuthor == null) {
            logger.info("Autor no actualizado. ID: {}", id);

            return ResponseHandler.sendResponse(
                    HttpStatus.NOT_FOUND, "Author not updated!", null
            );
        }

        logger.info("Autor actualizado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated author!", updatedAuthor
        );
    }

    /**
     * Endpoint para eliminar un autor por su ID
     *
     * @param id El identificador único del autor
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<AuthorResponseDto>> deleteAuthor(@PathVariable("id") long id) {
        authorService.deleteById(id);

        logger.info("Autor eliminado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted author!", null
        );
    }
}
