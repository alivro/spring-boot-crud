package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.model.author.response.BookOfAuthorResponseDto;
import com.alivro.spring.crud.service.IAuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class AuthorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IAuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private static AuthorSaveRequestDto authorSaveRequestVerne;
    private static AuthorSaveRequestDto authorUpdateRequestVerne;
    private static AuthorFindResponseDto authorResponseOrwell;
    private static AuthorFindResponseDto authorResponseWells;
    private static AuthorFindResponseDto authorResponseHuxley;
    private static AuthorFindResponseDto authorResponseCarroll;
    private static AuthorSaveResponseDto authorSavedResponseVerne;
    private static AuthorSaveResponseDto authorUpdatedResponseVerne;

    @BeforeAll
    public static void setup() {
        BookOfAuthorResponseDto book1984 = BookOfAuthorResponseDto.builder()
                .id(1L)
                .title("1984")
                .subtitle("75th Anniversary")
                .publisher("Berkley")
                .isbn13("'9780452284234'")
                .build();

        BookOfAuthorResponseDto bookTimeMachine = BookOfAuthorResponseDto.builder()
                .id(2L)
                .title("The Time Machine")
                .publisher("Penguin Classics")
                .isbn13("9780141439976")
                .build();

        BookOfAuthorResponseDto bookBraveWorld = BookOfAuthorResponseDto.builder()
                .id(3L)
                .title("Brave New World")
                .publisher("Harper")
                .isbn13("9780062696120")
                .build();

        authorResponseOrwell = AuthorFindResponseDto.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .books(Collections.singletonList(book1984))
                .build();

        authorResponseWells = AuthorFindResponseDto.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .books(Collections.singletonList(bookTimeMachine))
                .build();

        authorResponseHuxley = AuthorFindResponseDto.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .books(Collections.singletonList(bookBraveWorld))
                .build();

        authorResponseCarroll = AuthorFindResponseDto.builder()
                .id(4L)
                .firstName("Charles")
                .middleName("Lutwidge")
                .lastName("Dodgson")
                .pseudonym("Lewis Carroll")
                .books(new ArrayList<>())
                .build();

        authorSaveRequestVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gaby")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorSavedResponseVerne = mapRequestDtoToResponseDto(5L, authorSaveRequestVerne);

        authorUpdateRequestVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gabriel")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorUpdatedResponseVerne = mapRequestDtoToResponseDto(5L, authorUpdateRequestVerne);
    }

    @Test
    public void findAll_Authors_ExistingAuthors_Return_Ok() throws Exception {
        //Given
        List<AuthorFindResponseDto> foundAuthors = new ArrayList<>();
        foundAuthors.add(authorResponseOrwell);
        foundAuthors.add(authorResponseWells);
        foundAuthors.add(authorResponseHuxley);
        foundAuthors.add(authorResponseCarroll);

        given(authorService.findAll()).willReturn(foundAuthors);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/findAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found authors!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseOrwell.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].pseudonym",
                        CoreMatchers.is(authorResponseWells.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].pseudonym",
                        CoreMatchers.is(authorResponseHuxley.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].pseudonym",
                        CoreMatchers.is(authorResponseCarroll.getPseudonym())));
    }

    @Test
    public void findAll_Authors_NonExistingAuthors_Return_Ok() throws Exception {
        //Given
        List<AuthorFindResponseDto> foundAuthors = new ArrayList<>();

        given(authorService.findAll()).willReturn(foundAuthors);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/findAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found authors!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void findById_Author_ExistingAuthor_Return_Ok() throws Exception {
        //Given
        long authorId = 1L;

        given(authorService.findById(authorId)).willReturn(authorResponseOrwell);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found author!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(authorResponseOrwell.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorResponseOrwell.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorResponseOrwell.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorResponseOrwell.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseOrwell.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].books[0].title",
                        CoreMatchers.is(authorResponseOrwell.getBooks().get(0).getTitle())));
    }

    @Test
    public void findById_Author_NonExistingAuthor_Return_NotFound() throws Exception {
        //Given
        long authorId = 10L;

        given(authorService.findById(authorId))
                .willThrow(new DataNotFoundException("Author not found!"));

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Author not found!")));
    }

    @Test
    public void findById_Author_StringId_Return_InternalServerError() throws Exception {
        //Given
        String authorId = "one";

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_Author_NonExistingAuthor_Return_Created() throws Exception {
        // Given
        given(authorService.save(authorSaveRequestVerne))
                .willReturn(authorSavedResponseVerne);

        // When
        ResultActions response = mockMvc.perform(post("/api/author/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorSaveRequestVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved author!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorSavedResponseVerne.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorSavedResponseVerne.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorSavedResponseVerne.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorSavedResponseVerne.getPseudonym())));
    }

    @Test
    public void save_Author_ExistingAuthor_Return_Conflict() throws Exception {
        // Given
        given(authorService.save(any(AuthorSaveRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Author already exists!"));

        // When
        ResultActions response = mockMvc.perform(post("/api/author/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorSaveRequestVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Author already exists!")));
    }

    @Test
    public void update_Author_ExistingAuthor_Return_Ok() throws Exception {
        // Given
        long authorId = 5L;

        given(authorService.update(authorId, authorUpdateRequestVerne))
                .willReturn(authorUpdatedResponseVerne);

        // When
        ResultActions response = mockMvc.perform(put("/api/author/update/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorUpdateRequestVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated author!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorUpdatedResponseVerne.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorUpdatedResponseVerne.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorUpdatedResponseVerne.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorUpdatedResponseVerne.getPseudonym())));
    }

    @Test
    public void update_Author_NonExistingAuthor_Return_NotFound() throws Exception {
        // Given
        long authorId = 10L;

        given(authorService.update(anyLong(), any(AuthorSaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Author does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put("/api/author/update/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorUpdateRequestVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Author does not exist!")));
    }

    @Test
    public void deleteById_Author_Return_Ok() throws Exception {
        // Given
        long authorId = 1L;

        willDoNothing().given(authorService).deleteById(anyLong());

        // When
        ResultActions response = mockMvc.perform(delete("/api/author/delete/{id}", authorId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted author!")));
    }

    private static AuthorSaveResponseDto mapRequestDtoToResponseDto(long id, AuthorSaveRequestDto request) {
        return AuthorSaveResponseDto.builder()
                .id(id)
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .pseudonym(request.getPseudonym())
                .build();
    }
}
