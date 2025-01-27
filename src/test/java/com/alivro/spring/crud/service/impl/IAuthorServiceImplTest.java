package com.alivro.spring.crud.service.impl;

import com.alivro.spring.crud.exception.DataAlreadyExistsException;
import com.alivro.spring.crud.exception.DataNotFoundException;
import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.author.request.AuthorSaveRequestDto;
import com.alivro.spring.crud.model.author.response.AuthorFindResponseDto;
import com.alivro.spring.crud.model.author.response.AuthorSaveResponseDto;
import com.alivro.spring.crud.repository.AuthorRepository;
import com.alivro.spring.crud.util.CustomData;
import com.alivro.spring.crud.util.CustomPageMetadata;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private static Author authorGeorgeOrwell;
    private static Author authorHGWells;
    private static Author authorAldousHuxley;
    private static Author authorLewisCarroll;
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
                .build();

        Book bookTimeMachine = Book.builder()
                .id(2L)
                .title("The Time Machine")
                .totalPages(128)
                .publisher("Penguin Classics")
                .publishedDate(LocalDate.parse("2005-05-31"))
                .isbn13("9780141439976")
                .build();

        Book bookBraveWorld = Book.builder()
                .id(3L)
                .title("Brave New World")
                .totalPages(272)
                .publisher("Harper")
                .publishedDate(LocalDate.parse("2017-05-09"))
                .isbn13("9780062696120")
                .isbn10("0062696122")
                .build();

        Book bookAliceWonderland = Book.builder()
                .id(4L)
                .title("Alice''s Adventures in Wonderland")
                .totalPages(64)
                .publisher("'Penguin'")
                .publishedDate(LocalDate.parse("2023-02-02"))
                .isbn13("9780241588864")
                .build();

        authorGeorgeOrwell = Author.builder()
                .id(1L)
                .firstName("Eric")
                .middleName("Arthur")
                .lastName("Blair")
                .pseudonym("George Orwell")
                .books(Collections.singletonList(book1984))
                .build();

        authorHGWells = Author.builder()
                .id(2L)
                .firstName("Herbert")
                .middleName("George")
                .lastName("Wells")
                .pseudonym("H. G. Wells")
                .books(Collections.singletonList(bookTimeMachine))
                .build();

        authorAldousHuxley = Author.builder()
                .id(3L)
                .firstName("Aldous")
                .middleName("Leonard")
                .lastName("Huxley")
                .pseudonym("Aldous Huxley")
                .books(Collections.singletonList(bookBraveWorld))
                .build();

        authorLewisCarroll = Author.builder()
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
    public void findAllByPseudonymAsc_Authors_ExistingAuthors_Return_ListAuthorResponseDTO() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "pseudonym";

        List<Author> authors = new ArrayList<>();
        authors.add(authorAldousHuxley);
        authors.add(authorGeorgeOrwell);
        authors.add(authorHGWells);
        authors.add(authorLewisCarroll);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(authorRepository.findAll(pageable)).willReturn(
                new PageImpl<>(authors, pageable, authors.size())
        );

        // When
        CustomData<AuthorFindResponseDto, CustomPageMetadata> authorsData = authorService.findAll(
                PageRequest.of(0, 5, Sort.by("pseudonym").ascending())
        );

        // Then
        List<AuthorFindResponseDto> data = authorsData.getData();
        CustomPageMetadata meta = authorsData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getPseudonym()).isEqualTo("Aldous Huxley");
        assertThat(data.get(1).getPseudonym()).isEqualTo("George Orwell");
        assertThat(data.get(2).getPseudonym()).isEqualTo("H. G. Wells");
        assertThat(data.get(3).getPseudonym()).isEqualTo("Lewis Carroll");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAllByPseudonymDesc_Authors_ExistingAuthors_Return_ListAuthorResponseDTO() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "pseudonym";

        List<Author> authors = new ArrayList<>();
        authors.add(authorLewisCarroll);
        authors.add(authorHGWells);
        authors.add(authorGeorgeOrwell);
        authors.add(authorAldousHuxley);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).descending());

        given(authorRepository.findAll(pageable)).willReturn(
                new PageImpl<>(authors, pageable, authors.size())
        );

        // When
        CustomData<AuthorFindResponseDto, CustomPageMetadata> authorsData = authorService.findAll(
                PageRequest.of(0, 5, Sort.by("pseudonym").descending())
        );

        // Then
        List<AuthorFindResponseDto> data = authorsData.getData();
        CustomPageMetadata meta = authorsData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getPseudonym()).isEqualTo("Lewis Carroll");
        assertThat(data.get(1).getPseudonym()).isEqualTo("H. G. Wells");
        assertThat(data.get(2).getPseudonym()).isEqualTo("George Orwell");
        assertThat(data.get(3).getPseudonym()).isEqualTo("Aldous Huxley");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAll_Authors_NonExistingAuthors_Return_EmptyListAuthorResponseDTO() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Author> authors = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(authorRepository.findAll(pageable)).willReturn(
                new PageImpl<>(authors, pageable, authors.size())
        );

        // When
        CustomData<AuthorFindResponseDto, CustomPageMetadata> authorsData = authorService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<AuthorFindResponseDto> data = authorsData.getData();
        CustomPageMetadata meta = authorsData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findById_Author_ExistingAuthor_Return_AuthorResponseDTO() {
        // Given
        long authorId = 1L;

        given(authorRepository.findById(authorId)).willReturn(Optional.of(authorGeorgeOrwell));

        // When
        AuthorFindResponseDto foundAuthor = authorService.findById(authorId);

        // Then
        assertThat(foundAuthor).isNotNull();
        assertThat(foundAuthor.getId()).isEqualTo(authorId);
        assertThat(foundAuthor.getFirstName()).isEqualTo("Eric");
        assertThat(foundAuthor.getLastName()).isEqualTo("Blair");
        assertThat(foundAuthor.getPseudonym()).isEqualTo("George Orwell");
        assertThat(foundAuthor.getBooks().size()).isEqualTo(1);
        assertThat(foundAuthor.getBooks().get(0).getTitle()).isEqualTo("1984");
    }

    @Test
    public void findById_Author_NonExistingAuthor_Return_Null() {
        // Given
        long authorID = 10L;

        given(authorRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> authorService.findById(authorID));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Author not found!"));
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
        Throwable thrown = assertThrows(DataAlreadyExistsException.class,
                () -> authorService.save(authorSaveRequestVerne));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Author already exists!"));
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
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> authorService.update(authorId, authorUpdateRequestVerne));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Author does not exist!"));
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
