package com.alivro.spring.crud.services.impl;

import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.request.AuthorRequestDto;
import com.alivro.spring.crud.model.response.AuthorResponseDto;
import com.alivro.spring.crud.repository.AuthorRepository;
import com.alivro.spring.crud.services.IAuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<AuthorResponseDto> findAll() {
        logger.info("Busca todos los autores.");

        List<Author> foundAuthors = authorRepository.findAll();

        return foundAuthors.stream()
                .map(AuthorResponseDto::mapEntityToResponseDto)
                .toList();
    }

    /**
     * Método para buscar un autor por su ID
     *
     * @param id Identificador único del autor
     * @return Información del autor buscado
     */
    @Override
    public AuthorResponseDto findById(Long id) {
        logger.info("Busca autor. ID: {}", id);

        Optional<Author> foundAuthor = authorRepository.findById(id);

        return foundAuthor.map(AuthorResponseDto::mapEntityToResponseDto).orElse(null);
    }

    /**
     * Método para guardar un nuevo autor
     *
     * @param author Información del autor a guardar
     * @return Información del autor guardado
     */
    @Override
    public AuthorResponseDto save(AuthorRequestDto author) {
        String pseudonym = author.getPseudonym();

        logger.info("Busca autor. Pseudónimo: {}", pseudonym);

        // Verifica si ya existe un autor con el mismo pseudónimo
        if (authorRepository.existsByPseudonym(pseudonym)) {
            logger.info("Autor existente. Pseudónimo: {}", pseudonym);

            return null;
        }

        logger.info("Autor no existente. Pseudónimo: {}", pseudonym);
        logger.info("Guarda autor. Pseudónimo: {}", pseudonym);

        // Guarda el nuevo autor
        return AuthorResponseDto.mapEntityToResponseDto(
                authorRepository.save(
                        AuthorRequestDto.mapRequestDtoToEntity(author)
                )
        );
    }

    /**
     * Método para actualizar un autor
     *
     * @param id     Identificador único del autor
     * @param author Información del autor a actualizar
     * @return Información del autor actualizado
     */
    @Override
    public AuthorResponseDto update(Long id, AuthorRequestDto author) {
        logger.info("Busca autor. ID: {}", id);

        // Verifica si existe un autor con ese id
        if (!authorRepository.existsById(id)) {
            logger.info("Autor no existente. ID: {}", id);

            return null;
        }

        logger.info("Actualiza autor. ID: {}", id);

        // Actualiza la informaciṕn del autor
        return AuthorResponseDto.mapEntityToResponseDto(
                authorRepository.save(
                        AuthorRequestDto.mapRequestDtoToEntity(id, author)
                )
        );
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
