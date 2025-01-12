package com.alivro.spring.crud.service.impl;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.repository.AuthorRepository;
import com.alivro.spring.crud.service.IAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IAuthorServiceImpl implements IAuthorService {
    private final AuthorRepository authorRepository;
    private final Logger logger = LoggerFactory.getLogger(IAuthorServiceImpl.class);

    /**
     * Constructor
     *
     * @param authorRepository Author repository
     */
    @Autowired
    public IAuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Método para buscar todos los autores
     *
     * @return Información de todos los autores
     */
    @Override
    public List<AuthorFindResponseDto> findAll(Pageable pageable) {
        logger.info("Busca todos los autores.");

        Page<Author> foundAuthors = authorRepository.findAll(pageable);

        return foundAuthors.stream()
                .map(AuthorFindResponseDto::mapEntityToResponseDto)
                .toList();
    }

    /**
     * Método para buscar un autor por su ID
     *
     * @param id Identificador único del autor
     * @return Información del autor buscado
     */
    @Override
    public AuthorFindResponseDto findById(Long id) {
        logger.info("Busca autor. ID: {}", id);

        Optional<Author> foundAuthor = authorRepository.findById(id);

        if (foundAuthor.isEmpty()) {
            logger.info("Autor no encontrado. ID: {}", id);

            throw new DataNotFoundException("Author not found!");
        }

        return AuthorFindResponseDto.mapEntityToResponseDto(foundAuthor.get());
    }

    /**
     * Método para guardar un nuevo autor
     *
     * @param author Información del autor a guardar
     * @return Información del autor guardado
     */
    @Override
    public AuthorSaveResponseDto save(AuthorSaveRequestDto author) {
        String pseudonym = author.getPseudonym();

        logger.info("Busca autor. Pseudónimo: {}", pseudonym);

        // Verifica si ya existe un autor con el mismo pseudónimo
        if (authorRepository.existsByPseudonym(pseudonym)) {
            logger.info("Autor existente. Pseudónimo: {}", pseudonym);
            logger.info("Autor no guardado. Pseudónimo: {}", pseudonym);

            throw new DataAlreadyExistsException("Author already exists!");
        }

        logger.info("Autor no existente. Pseudónimo: {}", pseudonym);
        logger.info("Guarda autor. Pseudónimo: {}", pseudonym);

        // Guarda la información del nuevo autor
        Author savedAuthor = authorRepository.save(
                AuthorSaveRequestDto.mapRequestDtoToEntity(author)
        );

        return AuthorSaveResponseDto.mapEntityToResponseDto(savedAuthor);
    }

    /**
     * Método para actualizar un autor
     *
     * @param id     Identificador único del autor
     * @param author Información del autor a actualizar
     * @return Información del autor actualizado
     */
    @Override
    public AuthorSaveResponseDto update(Long id, AuthorSaveRequestDto author) {
        logger.info("Busca autor. ID: {}", id);

        Optional<Author> foundAuthor = authorRepository.findById(id);

        // Verifica si existe un autor con ese id
        if (foundAuthor.isEmpty()) {
            logger.info("Autor no existente. ID: {}", id);
            logger.info("Autor no actualizado. ID: {}", id);

            throw new DataNotFoundException("Author does not exist!");
        }

        // Información del autor a actualizar
        Author authorToUpdate = foundAuthor.get();
        authorToUpdate.setFirstName(author.getFirstName());
        authorToUpdate.setMiddleName(author.getMiddleName());
        authorToUpdate.setLastName(author.getLastName());
        authorToUpdate.setPseudonym(author.getPseudonym());

        logger.info("Actualiza autor. ID: {}", id);

        // Actualiza la información del autor
        Author updatedAuthor = authorRepository.save(authorToUpdate);

        return AuthorSaveResponseDto.mapEntityToResponseDto(updatedAuthor);
    }

    /**
     * Método para eliminar un autor por su ID
     *
     * @param id Identificador único del autor
     */
    @Override
    public void deleteById(Long id) {
        logger.info("Elimina autor. ID: {}", id);

        authorRepository.deleteById(id);
    }
}
