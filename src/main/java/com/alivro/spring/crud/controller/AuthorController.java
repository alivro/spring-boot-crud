package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.handler.ResponseHandler;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.service.IAuthorService;
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
    public ResponseEntity<CustomResponse<AuthorFindResponseDto>> findAllAuthors() {
        List<AuthorFindResponseDto> foundAuthors = authorService.findAll();

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
    public ResponseEntity<CustomResponse<AuthorFindResponseDto>> findAuthor(@PathVariable("id") long id) {
        AuthorFindResponseDto foundAuthor = authorService.findById(id);

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
    public ResponseEntity<CustomResponse<AuthorSaveResponseDto>> saveAuthor(
            @Valid @RequestBody AuthorSaveRequestDto author) {
        AuthorSaveResponseDto savedAuthor = authorService.save(author);

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
    public ResponseEntity<CustomResponse<AuthorSaveResponseDto>> updateAuthor(
            @PathVariable("id") long id, @Valid @RequestBody AuthorSaveRequestDto author) {
        AuthorSaveResponseDto updatedAuthor = authorService.update(id, author);

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
    public ResponseEntity<CustomResponse<AuthorSaveResponseDto>> deleteAuthor(@PathVariable("id") long id) {
        authorService.deleteById(id);

        logger.info("Autor eliminado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted author!", null
        );
    }
}
