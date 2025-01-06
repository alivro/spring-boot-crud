package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.model.request.AuthorRequestDto;
import com.alivro.spring.crud.model.response.AuthorResponseDto;
import com.alivro.spring.crud.services.IAuthorService;
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

    private static AuthorRequestDto authorRequestSaveVerne;
    private static AuthorRequestDto authorRequestUpdateVerne;
    private static AuthorResponseDto authorResponseOrwell;
    private static AuthorResponseDto authorResponseWells;
    private static AuthorResponseDto authorResponseHuxley;
    private static AuthorResponseDto authorResponseCarroll;
    private static AuthorResponseDto authorResponseSavedVerne;
    private static AuthorResponseDto authorResponseUpdatedVerne;

    @BeforeAll
    public static void setup() {
        authorResponseOrwell = AuthorResponseDto.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .build();

        authorResponseWells = AuthorResponseDto.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .build();

        authorResponseHuxley = AuthorResponseDto.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .build();

        authorResponseCarroll = AuthorResponseDto.builder()
                .id(4L)
                .firstName("Charles")
                .middleName("Lutwidge")
                .lastName("Dodgson")
                .pseudonym("Lewis Carroll")
                .build();

        authorRequestSaveVerne = AuthorRequestDto.builder()
                .firstName("Jules")
                .middleName("Gaby")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorResponseSavedVerne = mapRequestDtoToResponseDto(5L, authorRequestSaveVerne);

        authorRequestUpdateVerne = AuthorRequestDto.builder()
                .firstName("Jules")
                .middleName("Gabriel")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorResponseUpdatedVerne = mapRequestDtoToResponseDto(5L, authorRequestUpdateVerne);
    }

    @Test
    public void findAll_Authors_ExistingAuthors_Return_Ok() throws Exception {
        //Given
        List<AuthorResponseDto> foundAuthors = new ArrayList<>();
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
        List<AuthorResponseDto> foundAuthors = new ArrayList<>();

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
        long authorID = 1L;

        given(authorService.findById(authorID)).willReturn(authorResponseOrwell);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorID));

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
                        CoreMatchers.is(authorResponseOrwell.getPseudonym())));
    }

    @Test
    public void findById_Author_NonExistingAuthor_Return_NotFound() throws Exception {
        //Given
        long authorID = 10L;

        given(authorService.findById(authorID)).willReturn(null);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorID));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Author not found!")));
    }

    @Test
    public void save_Author_NonExistingAuthor_Return_Created() throws Exception {
        // Given
        given(authorService.save(authorRequestSaveVerne)).willReturn(authorResponseSavedVerne);

        // When
        ResultActions response = mockMvc.perform(post("/api/author/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequestSaveVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved author!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorResponseSavedVerne.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorResponseSavedVerne.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorResponseSavedVerne.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseSavedVerne.getPseudonym())));
    }

    @Test
    public void save_Author_ExistingAuthor_Return_Conflict() throws Exception {
        // Given
        given(authorService.save(any(AuthorRequestDto.class))).willReturn(null);

        // When
        ResultActions response = mockMvc.perform(post("/api/author/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequestSaveVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Author not saved!")));
    }

    @Test
    public void update_Author_ExistingAuthor_Return_Ok() throws Exception {
        // Given
        long authorId = 5L;

        given(authorService.update(authorId, authorRequestUpdateVerne)).willReturn(authorResponseUpdatedVerne);

        // When
        ResultActions response = mockMvc.perform(put("/api/author/update/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequestUpdateVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated author!")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorResponseUpdatedVerne.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorResponseUpdatedVerne.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorResponseUpdatedVerne.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseUpdatedVerne.getPseudonym())));
    }

    @Test
    public void update_Author_NonExistingAuthor_Return_NotFound() throws Exception {
        // Given
        long authorId = 10L;

        given(authorService.update(anyLong(), any(AuthorRequestDto.class))).willReturn(null);

        // When
        ResultActions response = mockMvc.perform(put("/api/author/update/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequestUpdateVerne)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Author not updated!")));
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

    private static AuthorResponseDto mapRequestDtoToResponseDto(long id, AuthorRequestDto request) {
        return AuthorResponseDto.builder()
                .id(id)
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .pseudonym(request.getPseudonym())
                .build();
    }
}
