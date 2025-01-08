package com.alivro.spring.crud.service.impl;

import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    private static AuthorSaveRequestDto authorSaveRequestVerne;
    private static AuthorSaveRequestDto authorUpdateRequestVerne;
    private static Author authorOrwell;
    private static Author authorWells;
    private static Author authorHuxley;
    private static Author authorCarroll;
    private static Author authorToSaveVerne;
    private static Author authorSavedVerne;
    private static Author authorToUpdateVerne;
    private static Author authorUpdatedVerne;

    @BeforeAll
    public static void setup() {
        Book book1984 = Book.builder()
                .id(1L)
                .title("1984")
                .subtitle("75th Anniversary")
                .totalPages(384)
                .publisher("Berkley")
                .publishedDate(LocalDate.parse("2003-05-06"))
                .isbn13("'9780452284234'")
                .isbn10(null)
                .build();

        Book bookTimeMachine = Book.builder()
                .id(2L)
                .title("The Time Machine")
                .subtitle(null)
                .totalPages(128)
                .publisher("Penguin Classics")
                .publishedDate(LocalDate.parse("2005-05-31"))
                .isbn13("9780141439976")
                .isbn10(null)
                .build();

        Book bookBraveWorld = Book.builder()
                .id(3L)
                .title("Brave New World")
                .subtitle(null)
                .totalPages(272)
                .publisher("Harper")
                .publishedDate(LocalDate.parse("2017-05-09"))
                .isbn13("9780062696120")
                .isbn10("0062696122")
                .build();

        authorOrwell = Author.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .books(Collections.singletonList(book1984))
                .build();

        authorWells = Author.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .books(Collections.singletonList(bookTimeMachine))
                .build();

        authorHuxley = Author.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .books(Collections.singletonList(bookBraveWorld))
                .build();

        authorCarroll = Author.builder()
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

        authorToSaveVerne = AuthorSaveRequestDto.mapRequestDtoToEntity(authorSaveRequestVerne);

        authorSavedVerne = AuthorSaveRequestDto.mapRequestDtoToEntity(5L, authorSaveRequestVerne);

        authorUpdateRequestVerne = AuthorSaveRequestDto.builder()
                .firstName("Jules")
                .middleName("Gabriel")
                .lastName("Verne")
                .pseudonym("Jules Verne")
                .build();

        authorToUpdateVerne = AuthorSaveRequestDto.mapRequestDtoToEntity(5L, authorUpdateRequestVerne);

        authorUpdatedVerne = AuthorSaveRequestDto.mapRequestDtoToEntity(5L, authorUpdateRequestVerne);
    }

    @Test
    public void findAll_Authors_ExistingAuthors_Return_ListAuthorResponseDTO() {
        // Given
        List<Author> authors = new ArrayList<>();
        authors.add(authorOrwell);
        authors.add(authorWells);
        authors.add(authorHuxley);
        authors.add(authorCarroll);

        given(authorRepository.findAll()).willReturn(authors);

        // When
        List<AuthorFindResponseDto> foundAuthors = authorService.findAll();

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
        List<AuthorFindResponseDto> foundAuthors = authorService.findAll();

        // Then
        assertThat(foundAuthors).isEmpty();
    }

    @Test
    public void findById_Author_ExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        long authorId = 1L;

        given(authorRepository.findById(authorId)).willReturn(Optional.of(authorOrwell));

        // When
        AuthorFindResponseDto foundAuthor = authorService.findById(authorId);

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
        AuthorFindResponseDto foundAuthor = authorService.findById(authorID);

        // Then
        assertThat(foundAuthor).isNull();
    }

    @Test
    public void save_Author_NonExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        given(authorRepository.existsByPseudonym(authorSaveRequestVerne.getPseudonym())).willReturn(false);
        given(authorRepository.save(authorToSaveVerne)).willReturn(authorSavedVerne);

        // When
        AuthorSaveResponseDto savedAuthor = authorService.save(authorSaveRequestVerne);

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
        AuthorSaveResponseDto savedAuthor = authorService.save(authorSaveRequestVerne);

        // Then
        assertThat(savedAuthor).isNull();
    }

    @Test
    public void update_Author_ExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        long authorId = 5L;

        given(authorRepository.findById(authorId)).willReturn(Optional.ofNullable(authorToUpdateVerne));
        given(authorRepository.save(authorToUpdateVerne)).willReturn(authorUpdatedVerne);

        // When
        AuthorSaveResponseDto updatedAuthor = authorService.update(authorId, authorUpdateRequestVerne);

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

        given(authorRepository.findById(authorId)).willReturn(Optional.empty());

        // When
        AuthorSaveResponseDto updatedAuthor = authorService.update(authorId, authorUpdateRequestVerne);

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
