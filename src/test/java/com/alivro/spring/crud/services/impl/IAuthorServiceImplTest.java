package com.alivro.spring.crud.services.impl;

import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.request.AuthorRequestDto;
import com.alivro.spring.crud.model.response.AuthorResponseDto;
import com.alivro.spring.crud.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IAuthorServiceImplTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private IAuthorServiceImpl authorService;

    private static AuthorRequestDto authorRequestSaveVerne;
    private static AuthorRequestDto authorRequestUpdateVerne;
    private static Author authorOrwell;
    private static Author authorWells;
    private static Author authorHuxley;
    private static Author authorCarroll;
    private static Author authorSaveVerne;
    private static Author authorSavedVerne;
    private static Author authorUpdateVerne;
    private static Author authorUpdatedVerne;

    @BeforeAll
    public static void setup() {
        authorOrwell = Author.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .build();

        authorWells = Author.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .build();

        authorHuxley = Author.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .build();

        authorCarroll = Author.builder()
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

        authorSaveVerne = AuthorRequestDto.mapRequestDtoToEntity(authorRequestSaveVerne);

        authorSavedVerne = AuthorRequestDto.mapRequestDtoToEntity(5L, authorRequestSaveVerne);

        authorRequestUpdateVerne = AuthorRequestDto.builder()
                .firstName("Jules")
                .middleName("Gabriel")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorUpdateVerne = AuthorRequestDto.mapRequestDtoToEntity(5L, authorRequestUpdateVerne);

        authorUpdatedVerne = AuthorRequestDto.mapRequestDtoToEntity(5L, authorRequestUpdateVerne);
    }

    @Test
    public void findAll_Author_ExistingAuthors_Return_ListAuthorResponseDTO() {
        // Given
        List<Author> authors = new ArrayList<>();
        authors.add(authorOrwell);
        authors.add(authorWells);
        authors.add(authorHuxley);
        authors.add(authorCarroll);

        given(authorRepository.findAll()).willReturn(authors);

        // When
        List<AuthorResponseDto> foundAuthors = authorService.findAll();

        // Then
        assertThat(foundAuthors.size()).isEqualTo(4);
        assertThat(foundAuthors.get(0).getPseudonym()).isEqualTo("George Orwell");
        assertThat(foundAuthors.get(1).getPseudonym()).isEqualTo("H. G. Wells");
        assertThat(foundAuthors.get(2).getPseudonym()).isEqualTo("Aldous Huxley");
        assertThat(foundAuthors.get(3).getPseudonym()).isEqualTo("Lewis Carroll");
    }

    @Test
    public void findAll_Authors_NonExistingAuthors_Return_EmptyListAuthorResponseDTO() {
        // Given
        List<Author> authors = new ArrayList<>();

        given(authorRepository.findAll()).willReturn(authors);

        // When
        List<AuthorResponseDto> foundAuthors = authorService.findAll();

        // Then
        assertThat(foundAuthors).isEmpty();
    }

    @Test
    public void findById_Author_ExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        long authorId = 1L;

        given(authorRepository.findById(authorId)).willReturn(Optional.of(authorOrwell));

        // When
        AuthorResponseDto foundAuthor = authorService.findById(authorId);

        // Then
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getId()).isEqualTo(authorId);
        assertThat(foundAuthor.getFirstName()).isEqualTo("Eric");
        assertThat(foundAuthor.getLastName()).isEqualTo("Blair");
        assertThat(foundAuthor.getPseudonym()).isEqualTo("George Orwell");
    }

    @Test
    public void findById_Author_NonExistingAuthor_Return_Null() {
        // Given
        long authorID = 10L;

        given(authorRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        AuthorResponseDto foundAuthor = authorService.findById(authorID);

        // Then
        assertThat(foundAuthor).isNull();
    }

    @Test
    public void save_Author_NonExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        given(authorRepository.existsByPseudonym(authorRequestSaveVerne.getPseudonym())).willReturn(false);
        given(authorRepository.save(authorSaveVerne)).willReturn(authorSavedVerne);

        // When
        AuthorResponseDto savedAuthor = authorService.save(authorRequestSaveVerne);

        // Then
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("Jules");
        assertThat(savedAuthor.getMiddleName()).isEqualTo("Gaby");
        assertThat(savedAuthor.getLastName()).isEqualTo("Verne");
        assertThat(savedAuthor.getPseudonym()).isEqualTo("Jules Verne");
    }

    @Test
    public void save_Author_ExistingAuthor_Return_Null() {
        // Given
        given(authorRepository.existsByPseudonym(anyString())).willReturn(true);

        // When
        AuthorResponseDto savedAuthor = authorService.save(authorRequestSaveVerne);

        // Then
        assertThat(savedAuthor).isNull();
    }

    @Test
    public void update_Author_ExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        long authorId = 5L;

        given(authorRepository.existsById(authorId)).willReturn(true);
        given(authorRepository.save(authorUpdateVerne)).willReturn(authorUpdatedVerne);

        // When
        AuthorResponseDto updatedAuthor = authorService.update(authorId, authorRequestUpdateVerne);

        // Then
        assertThat(updatedAuthor).isNotNull();
        assertThat(updatedAuthor.getFirstName()).isEqualTo("Jules");
        assertThat(updatedAuthor.getMiddleName()).isEqualTo("Gabriel");
        assertThat(updatedAuthor.getLastName()).isEqualTo("Verne");
        assertThat(updatedAuthor.getPseudonym()).isEqualTo("Jules Verne");
    }

    @Test
    public void update_Author_NonExistingAuthor_Return_Null() {
        // Given
        long authorId = 10L;

        given(authorRepository.existsById(anyLong())).willReturn(false);

        // When
        AuthorResponseDto updatedAuthor = authorService.update(authorId, authorRequestUpdateVerne);

        // Then
        assertThat(updatedAuthor).isNull();
    }

    @Test
    public void deleteById_Author_NoReturn() {
        // Given
        long authorId = 1L;

        willDoNothing().given(authorRepository).deleteById(anyLong());

        // When
        authorService.deleteById(authorId);

        // Then
        verify(authorRepository, times(1)).deleteById(authorId);
    }
}
