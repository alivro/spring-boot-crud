package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.SpringBootCrudApplication;
import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.book.request.AuthorOfBookRequestDto;
import com.alivro.spring.crud.model.book.request.BookSaveRequestDto;
import com.alivro.spring.crud.model.book.response.AuthorOfBookResponseDto;
import com.alivro.spring.crud.model.book.response.BookResponseDto;
import com.alivro.spring.crud.util.CustomErrorResponse;
import com.alivro.spring.crud.util.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SpringBootCrudApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static HttpHeaders headers;

    private static Book book1984;
    private static BookSaveRequestDto bookSaveRequestSQLGuide;
    private static BookSaveRequestDto bookSaveRequest1984;
    private static BookSaveRequestDto bookUpdateRequest1984;
    private static BookSaveRequestDto bookUpdateRequestSQLGuide;

    @BeforeAll
    public static void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Authors
        Author authorGeorgeOrwell = Author.builder()
                .id(1L)
                .firstName("Eric")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .build();

        // Books
        book1984 = Book.builder()
                .id(1L)
                .title("1984")
                .authors(Collections.singletonList(authorGeorgeOrwell))
                .totalPages(384)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("9780452284234")
                .build();

        // Request
        AuthorOfBookRequestDto authorRequestAliceZhao = AuthorOfBookRequestDto.builder()
                .id(12L)
                .pseudonym("Alice Zhao")
                .build();

        AuthorOfBookRequestDto authorRequestGeorgeOrwell = AuthorOfBookRequestDto.builder()
                .id(1L)
                .pseudonym("George Orwell")
                .build();

        bookSaveRequestSQLGuide = BookSaveRequestDto.builder()
                .title("SQL Pokcet Guide")
                .subtitle("A Guide to LQS Usage")
                .authors(Collections.singletonList(authorRequestAliceZhao))
                .totalPages(354)
                .publisher("O'Reilly Media")
                .publishedDate(LocalDate.parse("2021-10-05"))
                .isbn13("9781492090403")
                .isbn10("1492090409")
                .build();

        bookSaveRequest1984 = BookSaveRequestDto.builder()
                .title("1984")
                .authors(Collections.singletonList(authorRequestGeorgeOrwell))
                .totalPages(384)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("9780452284234")
                .build();

        bookUpdateRequest1984 = BookSaveRequestDto.builder()
                .title("1984")
                .subtitle("75th Anniversary")
                .authors(Collections.singletonList(authorRequestGeorgeOrwell))
                .totalPages(384)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("9780452284234")
                .build();

        bookUpdateRequestSQLGuide = BookSaveRequestDto.builder()
                .title("SQL Pocket Guide")
                .subtitle("A Guide to SQL Usage")
                .authors(Collections.singletonList(authorRequestAliceZhao))
                .totalPages(354)
                .publisher("O'Reilly Media")
                .publishedDate(LocalDate.parse("2021-10-05"))
                .isbn13("9781492090403")
                .isbn10("1492090409")
                .build();
    }

    @Test
    @Order(1)
    public void findAll_Books_ExistingBooks_Return_Ok() {
        // When
        String url = "/findAll";

        ResponseEntity<CustomResponse<BookResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        List<BookResponseDto> books = Objects.requireNonNull(response.getBody()).getData();
        HashMap metadata = (HashMap) response.getBody().getMetadata();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Found books!");

        assertThat(books.size()).isGreaterThan(0);

        assertThat(metadata.get("pageNumber")).isEqualTo(0);
        assertThat(metadata.get("pageSize")).isEqualTo(5);
        assertThat(metadata.get("numberOfElements")).isEqualTo(books.size());
    }

    @Test
    @Order(2)
    public void findById_Book_ExistingBook_Return_Ok() {
        // When
        String url = "/find/1";

        ResponseEntity<CustomResponse<BookResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        BookResponseDto book = Objects.requireNonNull(response.getBody()).getData().get(0);
        List<AuthorOfBookResponseDto> authors = (book != null) ? book.getAuthors() : null;

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Found book!");

        assert book != null;
        assertThat(book.getId()).isEqualTo(book1984.getId());
        assertThat(book.getTitle()).isEqualTo(book1984.getTitle());
        assertThat(book.getSubtitle()).isEqualTo(null);
        assertThat(book.getTotalPages()).isEqualTo(book1984.getTotalPages());
        assertThat(book.getIsbn13()).isEqualTo(book1984.getIsbn13());

        assertThat(authors.get(0).getPseudonym()).isEqualTo(book1984.getAuthors().get(0).getPseudonym());
    }

    @Test
    @Order(3)
    public void findById_Book_NonExistingBook_Return_NotFound() {
        // When
        String url = "/find/1000";

        ResponseEntity<CustomErrorResponse> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getError())
                .isEqualTo("Book not found!");
    }

    @Test
    @Order(4)
    public void findById_Book_StringId_Return_InternalServerError() {
        // When
        String url = "/find/one";

        ResponseEntity<CustomErrorResponse> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @Order(5)
    public void save_Book_NonExistingBook_Return_Created() throws JsonProcessingException {
        // When
        String url = "/save";
        String request = objectMapper.writeValueAsString(bookSaveRequestSQLGuide);

        ResponseEntity<CustomResponse<BookResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        BookResponseDto book = Objects.requireNonNull(response.getBody()).getData().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getMessage()).isEqualTo("Saved book!");

        assertThat(book.getId()).isGreaterThan(1000);
        assertThat(book.getTitle()).isEqualTo(bookSaveRequestSQLGuide.getTitle());
        assertThat(book.getSubtitle()).isEqualTo(bookSaveRequestSQLGuide.getSubtitle());
        assertThat(book.getTotalPages()).isEqualTo(bookSaveRequestSQLGuide.getTotalPages());
        assertThat(book.getIsbn13()).isEqualTo(bookSaveRequestSQLGuide.getIsbn13());
    }

    @Test
    @Order(6)
    public void save_Book_ExistingBook_Return_Conflict() throws JsonProcessingException {
        // When
        String url = "/save";
        String request = objectMapper.writeValueAsString(bookSaveRequest1984);

        ResponseEntity<CustomErrorResponse> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                CustomErrorResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Objects.requireNonNull(response.getBody()).getError())
                .isEqualTo("Book already exists!");
    }

    @Test
    @Order(7)
    public void update_Book_ExistingBook_Return_Ok() throws JsonProcessingException {
        // When
        String url = "/update/1";

        ResponseEntity<CustomResponse<BookResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.PUT,
                new HttpEntity<>(
                        objectMapper.writeValueAsString(bookUpdateRequest1984),
                        headers
                ),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        BookResponseDto book = Objects.requireNonNull(response.getBody()).getData().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Updated book!");

        assertThat(book.getId()).isEqualTo(1);
        assertThat(book.getTitle()).isEqualTo(bookUpdateRequest1984.getTitle());
        assertThat(book.getSubtitle()).isEqualTo(bookUpdateRequest1984.getSubtitle());
        assertThat(book.getTotalPages()).isEqualTo(bookUpdateRequest1984.getTotalPages());
        assertThat(book.getIsbn13()).isEqualTo(bookUpdateRequest1984.getIsbn13());
    }

    @Test
    @Order(8)
    public void update_Book_NonExistingBook_Return_NotFound() throws JsonProcessingException {
        // When
        String url = "/update/10001";
        String request = objectMapper.writeValueAsString(bookUpdateRequestSQLGuide);

        ResponseEntity<CustomErrorResponse> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.PUT,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getError())
                .isEqualTo("Book does not exist!");
    }

    @Test
    @Order(9)
    public void deleteById_Book_Return_Ok() {
        // When
        String url = "/delete/1";

        ResponseEntity<CustomResponse<BookResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage())
                .isEqualTo("Deleted book!");
    }

    private String createUrl(String uri) {
        return "http://localhost:" + port + "/api/book" + uri;
    }
}
