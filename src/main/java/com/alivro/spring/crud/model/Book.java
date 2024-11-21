package com.alivro.spring.crud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title", nullable=false, unique=false)
    private String title;

    @Column(name = "author", length=100, nullable=false, unique=false)
    private String author;

    @Column(name = "total_pages", nullable=false, unique=false)
    private int totalPages;

    @Column(name = "publisher", length=50, nullable=false, unique=false)
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "published_date", nullable=false, unique=false)
    private LocalDate publishedDate;

    @Column(name = "isbn", length=13, nullable=false, unique=true)
    private String isbn;
}
