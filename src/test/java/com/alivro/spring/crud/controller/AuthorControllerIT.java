package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.SpringBootCrudApplication;
import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.model.author.response.BookOfAuthorResponseDto;
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
import java.util.Arrays;
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
public class AuthorControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static HttpHeaders headers;

    private static Author authorGeorgeOrwell;
    private static AuthorSaveRequestDto authorSaveRequestJulesVerne;
    private static AuthorSaveRequestDto authorSaveRequestGeorgeOrwell;
    private static AuthorSaveRequestDto authorUpdateRequestGeorgeOrwell;
    private static AuthorSaveRequestDto authorUpdateRequestJulesVerne;

    @BeforeAll
    public static void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Books
        Book book1984 = Book.builder()
                .id(1L)
                .title("1984")
                .subtitle("75th Anniversary")
                .totalPages(384)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("9780452284234")
                .build();

        Book bookAnimalFarm = Book.builder()
                .id(2L)
                .title("Animal Farm")
                .subtitle("75th Anniversary")
                .totalPages(128)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("9780452284241")
                .build();

        // Authors
        authorGeorgeOrwell = Author.builder()
                .id(1L)
                .firstName("Eric")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .books(Arrays.asList(book1984, bookAnimalFarm))
                .build();

        // Request
        authorSaveRequestJulesVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gaby")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorSaveRequestGeorgeOrwell = AuthorSaveRequestDto.builder()
                .firstName("Erick")
                .middleName("Arturo")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .build();

        authorUpdateRequestGeorgeOrwell = AuthorSaveRequestDto.builder()
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .build();

        authorUpdateRequestJulesVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gabriel")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();
    }

    @Test
    @Order(1)
    public void findAll_Authors_ExistingAuthors_Return_Ok() {
        // When
        String url = "/findAll";

        ResponseEntity<CustomResponse<AuthorFindResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        List<AuthorFindResponseDto> authors = Objects.requireNonNull(response.getBody()).getData();
        HashMap metadata = (HashMap) response.getBody().getMetadata();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Found authors!");

        assertThat(authors.size()).isGreaterThan(0);

        assertThat(metadata.get("pageNumber")).isEqualTo(0);
        assertThat(metadata.get("pageSize")).isEqualTo(5);
        assertThat(metadata.get("numberOfElements")).isEqualTo(authors.size());
    }

    @Test
    @Order(2)
    public void findById_Author_ExistingAuthor_Return_Ok() {
        // When
        String url = "/find/1";

        ResponseEntity<CustomResponse<AuthorFindResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        AuthorFindResponseDto author = Objects.requireNonNull(response.getBody()).getData().get(0);
        List<BookOfAuthorResponseDto> books = (author != null) ? author.getBooks() : null;

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Found author!");

        assert author != null;
        assertThat(author.getId()).isEqualTo(authorGeorgeOrwell.getId());
        assertThat(author.getFirstName()).isEqualTo(authorGeorgeOrwell.getFirstName());
        assertThat(author.getMiddleName()).isEqualTo(authorGeorgeOrwell.getMiddleName());
        assertThat(author.getLastName()).isEqualTo(authorGeorgeOrwell.getLastName());
        assertThat(author.getPseudonym()).isEqualTo(authorGeorgeOrwell.getPseudonym());

        assertThat(books.get(0).getTitle()).isEqualTo(authorGeorgeOrwell.getBooks().get(0).getTitle());
        assertThat(books.get(1).getTitle()).isEqualTo(authorGeorgeOrwell.getBooks().get(1).getTitle());
    }

    @Test
    @Order(3)
    public void findById_Author_NonExistingAuthor_Return_NotFound() {
        // When
        String url = "/find/100";

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
                .isEqualTo("Author not found!");
    }

    @Test
    @Order(4)
    public void findById_Author_StringId_Return_InternalServerError() {
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
    public void save_Author_NonExistingAuthor_Return_Created() throws JsonProcessingException {
        // When
        String url = "/save";
        String request = objectMapper.writeValueAsString(authorSaveRequestJulesVerne);

        ResponseEntity<CustomResponse<AuthorSaveResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        AuthorSaveResponseDto author = Objects.requireNonNull(response.getBody()).getData().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getMessage()).isEqualTo("Saved author!");

        assertThat(author.getId()).isGreaterThan(100);
        assertThat(author.getFirstName()).isEqualTo(authorSaveRequestJulesVerne.getFirstName());
        assertThat(author.getMiddleName()).isEqualTo(authorSaveRequestJulesVerne.getMiddleName());
        assertThat(author.getLastName()).isEqualTo(authorSaveRequestJulesVerne.getLastName());
        assertThat(author.getPseudonym()).isEqualTo(authorSaveRequestJulesVerne.getPseudonym());
    }

    @Test
    @Order(6)
    public void save_Author_ExistingAuthor_Return_Conflict() throws JsonProcessingException {
        // When
        String url = "/save";
        String request = objectMapper.writeValueAsString(authorSaveRequestGeorgeOrwell);

        ResponseEntity<CustomErrorResponse> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                CustomErrorResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Objects.requireNonNull(response.getBody()).getError())
                .isEqualTo("Author already exists!");
    }

    @Test
    @Order(7)
    public void update_Author_ExistingAuthor_Return_Ok() throws JsonProcessingException {
        // When
        String url = "/update/1";

        ResponseEntity<CustomResponse<AuthorSaveResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.PUT,
                new HttpEntity<>(
                        objectMapper.writeValueAsString(authorUpdateRequestGeorgeOrwell),
                        headers
                ),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        AuthorSaveResponseDto author = Objects.requireNonNull(response.getBody()).getData().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage())
                .isEqualTo("Updated author!");

        assertThat(author.getId()).isEqualTo(1);
        assertThat(author.getFirstName()).isEqualTo(authorUpdateRequestGeorgeOrwell.getFirstName());
        assertThat(author.getMiddleName()).isEqualTo(authorUpdateRequestGeorgeOrwell.getMiddleName());
        assertThat(author.getLastName()).isEqualTo(authorUpdateRequestGeorgeOrwell.getLastName());
        assertThat(author.getPseudonym()).isEqualTo(authorUpdateRequestGeorgeOrwell.getPseudonym());
    }

    @Test
    @Order(8)
    public void update_Author_NonExistingAuthor_Return_NotFound() throws JsonProcessingException {
        // When
        String url = "/update/1001";
        String request = objectMapper.writeValueAsString(authorUpdateRequestJulesVerne);

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
                .isEqualTo("Author does not exist!");
    }

    @Test
    @Order(9)
    public void deleteById_Author_Return_Ok() {
        // When
        String url = "/delete/1";

        ResponseEntity<CustomResponse<AuthorSaveResponseDto>> response = restTemplate.exchange(
                createUrl(url),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage())
                .isEqualTo("Deleted author!");
    }

    private String createUrl(String uri) {
        return "http://localhost:" + port + "/api/author" + uri;
    }
}
