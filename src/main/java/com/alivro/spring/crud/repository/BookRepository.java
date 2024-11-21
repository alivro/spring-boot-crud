package com.alivro.spring.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alivro.spring.crud.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Método para buscar la existencia de un libro por su ISBN
     *
     * @param   isbn    Identificador único del libro
     * @return          true si existe, en caso contrario, false
     */
    boolean existsByIsbn(String isbn);
}
