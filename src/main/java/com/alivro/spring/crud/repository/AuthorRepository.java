package com.alivro.spring.crud.repository;

import com.alivro.spring.crud.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    /**
     * Método para buscar la existencia de un autor por su pseudónimo
     *
     * @param pseudonym Pseudónimo único del autor
     * @return true si existe, en caso contrario, false
     */
    boolean existsByPseudonym(String pseudonym);
}
