package com.alivro.spring.crud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder=true)
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookGen")
    @SequenceGenerator(name = "bookGen", sequenceName = "book_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "author")
    private String author;

    @Column(name = "total_pages")
    private int totalPages;

    @Column(name = "publisher")
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "isbn_13")
    private String isbn13;

    @Column(name = "isbn_10")
    private String isbn10;
}
