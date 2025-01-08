package com.alivro.spring.crud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorGen")
    @SequenceGenerator(name = "authorGen", sequenceName = "author_seq", allocationSize = 1)
    @Column(name = "author_id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pseudonym")
    private String pseudonym;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            inverseJoinColumns = @JoinColumn(name = "book_id"),
            joinColumns = @JoinColumn(name = "author_id")
    )
    private List<Book> books = new ArrayList<>();
}
