package com.alivro.spring.crud.repository;

import com.alivro.spring.crud.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Método para buscar la existencia de un libro por su ISBN-13
     *
     * @param isbn13 Identificador único del libro
     * @return true si existe, en caso contrario, false
     */
    boolean existsByIsbn13(String isbn13);
}
