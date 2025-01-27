package com.alivro.spring.crud.controller;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.model.author.response.BookOfAuthorResponseDto;
import com.alivro.spring.crud.service.IAuthorService;
import com.alivro.spring.crud.util.CustomData;
import com.alivro.spring.crud.util.CustomPageMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static AuthorFindResponseDto authorResponseGeorgeOrwell;
    private static AuthorFindResponseDto authorResponseHGWells;
    private static AuthorFindResponseDto authorResponseAldousHuxley;
    private static AuthorFindResponseDto authorResponseLewisCarroll;
    private static AuthorSaveRequestDto authorSaveRequestVerne;
    private static AuthorSaveRequestDto authorUpdateRequestVerne;
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

        BookOfAuthorResponseDto bookAliceWonderland = BookOfAuthorResponseDto.builder()
                .id(4L)
                .title("Alice''s Adventures in Wonderland")
                .publisher("'Penguin'")
                .isbn13("9780241588864")
                .build();

        authorResponseGeorgeOrwell = AuthorFindResponseDto.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .books(Collections.singletonList(book1984))
                .build();

        authorResponseHGWells = AuthorFindResponseDto.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .books(Collections.singletonList(bookTimeMachine))
                .build();

        authorResponseAldousHuxley = AuthorFindResponseDto.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .books(Collections.singletonList(bookBraveWorld))
                .build();

        authorResponseLewisCarroll = AuthorFindResponseDto.builder()
                .id(4L)
                .firstName("Charles")
                .middleName("Lutwidge")
                .lastName("Dodgson")
                .pseudonym("Lewis Carroll")
                .books(Collections.singletonList(bookAliceWonderland))
                .build();

        authorSaveRequestVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gaby")
                .lastName("Verne")
                .pseudonym("Jules Verne")
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
    public void findAllPseudonymAsc_Authors_ExistingAuthors_Return_Ok() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "pseudonym";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<AuthorFindResponseDto> foundAuthors = new ArrayList<>();
        foundAuthors.add(authorResponseAldousHuxley);
        foundAuthors.add(authorResponseGeorgeOrwell);
        foundAuthors.add(authorResponseHGWells);
        foundAuthors.add(authorResponseLewisCarroll);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundAuthors.size())
                .totalPages((int) Math.ceil((double) foundAuthors.size() / pageSize))
                .totalElements(foundAuthors.size())
                .build();

        given(authorService.findAll(pageable)).willReturn(
                CustomData.<AuthorFindResponseDto, CustomPageMetadata>builder()
                        .data(foundAuthors)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get("/api/author/findAll")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "pseudonym,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found authors!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseAldousHuxley.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].pseudonym",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].pseudonym",
                        CoreMatchers.is(authorResponseHGWells.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].pseudonym",
                        CoreMatchers.is(authorResponseLewisCarroll.getPseudonym())));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundAuthors.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundAuthors.size())));
    }

    @Test
    public void findAllPseudonymDesc_Authors_ExistingAuthors_Return_Ok() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "pseudonym";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).descending());

        List<AuthorFindResponseDto> foundAuthors = new ArrayList<>();
        foundAuthors.add(authorResponseLewisCarroll);
        foundAuthors.add(authorResponseHGWells);
        foundAuthors.add(authorResponseGeorgeOrwell);
        foundAuthors.add(authorResponseAldousHuxley);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundAuthors.size())
                .totalPages((int) Math.ceil((double) foundAuthors.size() / pageSize))
                .totalElements(foundAuthors.size())
                .build();

        given(authorService.findAll(pageable)).willReturn(
                CustomData.<AuthorFindResponseDto, CustomPageMetadata>builder()
                        .data(foundAuthors)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get("/api/author/findAll")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "pseudonym,desc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found authors!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseLewisCarroll.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].pseudonym",
                        CoreMatchers.is(authorResponseHGWells.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].pseudonym",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].pseudonym",
                        CoreMatchers.is(authorResponseAldousHuxley.getPseudonym())));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundAuthors.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundAuthors.size())));
    }

    @Test
    public void findAll_Authors_NonExistingAuthors_Return_Ok() throws Exception {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<AuthorFindResponseDto> foundAuthors = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundAuthors.size())
                .totalPages((int) Math.ceil((double) foundAuthors.size() / pageSize))
                .totalElements(foundAuthors.size())
                .build();

        given(authorService.findAll(pageable)).willReturn(
                CustomData.<AuthorFindResponseDto, CustomPageMetadata>builder()
                        .data(foundAuthors)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get("/api/author/findAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found authors!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundAuthors.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundAuthors.size())));
    }

    @Test
    public void findById_Author_ExistingAuthor_Return_Ok() throws Exception {
        //Given
        long authorId = 1L;

        given(authorService.findById(authorId)).willReturn(authorResponseGeorgeOrwell);

        // When
        ResultActions response = mockMvc.perform(get("/api/author/find/{id}", authorId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found author!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(authorResponseGeorgeOrwell.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].middleName",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getMiddleName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].pseudonym",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getPseudonym())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].books[0].title",
                        CoreMatchers.is(authorResponseGeorgeOrwell.getBooks().get(0).getTitle())));
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
                        CoreMatchers.is("Saved author!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
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
                        CoreMatchers.is("Updated author!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
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
