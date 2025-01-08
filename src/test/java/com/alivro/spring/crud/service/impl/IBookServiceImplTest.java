package com.alivro.spring.crud.service.impl;

import com.alivro.spring.crud.model.Author;
import com.alivro.spring.crud.model.Book;
import com.alivro.spring.crud.model.book.request.AuthorOfBookRequestDto;
import com.alivro.spring.crud.model.book.request.BookSaveRequestDto;
import com.alivro.spring.crud.model.book.response.BookResponseDto;
import com.alivro.spring.crud.repository.BookRepository;
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
public class IBookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private IBookServiceImpl bookService;

    private static BookSaveRequestDto bookSaveRequestAustereAcademy;
    private static BookSaveRequestDto bookUpdateRequestAustereAcademy;
    private static Book bookBadBeginning;
    private static Book bookReptileRoom;
    private static Book bookWideWindow;
    private static Book bookMiserableMill;
    private static Book bookToSaveAustereAcademy;
    private static Book bookSavedAustereAcademy;
    private static Book bookToUpdateAustereAcademy;
    private static Book bookUpdatedAustereAcademy;

    @BeforeAll
    public static void setup() {
        Author authorSnicket = Author.builder()
                .id(1L)
                .firstName("Daniel")
                .lastName("Handler")
                .pseudonym("Lemony Snicket")
                .build();

        bookBadBeginning = Book.builder()
                .id(1L)
                .title("A Series of Unfortunate Events")
                .subtitle("The Bad Beginning")
                .authors(Collections.singletonList(authorSnicket))
                .totalPages(176)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("1999-08-25"))
                .isbn13("9780064407663")
                .build();

        bookReptileRoom = Book.builder()
                .id(2L)
                .title("A Series of Unfortunate Events")
                .subtitle("The Reptile Room")
                .authors(Collections.singletonList(authorSnicket))
                .totalPages(208)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("1999-08-25"))
                .isbn13("9780064407670")
                .build();

        bookWideWindow = Book.builder()
                .id(3L)
                .title("A Series of Unfortunate Events")
                .subtitle("The Wide Window")
                .authors(Collections.singletonList(authorSnicket))
                .totalPages(224)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("2000-02-02"))
                .isbn13("9780064407687")
                .build();

        bookMiserableMill = Book.builder()
                .id(4L)
                .title("A Series of Unfortunate Events")
                .subtitle("The Miserable Mill")
                .authors(Collections.singletonList(authorSnicket))
                .totalPages(208)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("2000-04-05"))
                .isbn13("9780064407694")
                .build();

        AuthorOfBookRequestDto booksAuthorRequestSnicket = AuthorOfBookRequestDto.builder()
                .id(1L)
                .pseudonym("Lemony Snicket")
                .build();

        bookSaveRequestAustereAcademy = BookSaveRequestDto.builder()
                .title("A Series of Fortunate Events")
                .subtitle("The Ostentatious Academy")
                .authors(Collections.singletonList(booksAuthorRequestSnicket))
                .totalPages(231)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("2009-10-13"))
                .isbn13("9780064408639")
                .build();

        bookToSaveAustereAcademy = BookSaveRequestDto.mapRequestDtoToEntity(bookSaveRequestAustereAcademy);

        bookSavedAustereAcademy = BookSaveRequestDto.mapRequestDtoToEntity(5L, bookSaveRequestAustereAcademy);

        bookUpdateRequestAustereAcademy = BookSaveRequestDto.builder()
                .title("A Series of Unfortunate Events")
                .subtitle("The Austere Academy")
                .authors(Collections.singletonList(booksAuthorRequestSnicket))
                .totalPages(231)
                .publisher("HarperCollins")
                .publishedDate(LocalDate.parse("2009-10-13"))
                .isbn13("9780064408639")
                .build();

        bookToUpdateAustereAcademy = BookSaveRequestDto.mapRequestDtoToEntity(5L, bookUpdateRequestAustereAcademy);

        bookUpdatedAustereAcademy = BookSaveRequestDto.mapRequestDtoToEntity(5L, bookUpdateRequestAustereAcademy);
    }

    @Test
    public void findAll_Books_ExistingBooks_Return_ListBookResponseDTO() {
        // Given
        List<Book> books = new ArrayList<>();
        books.add(bookBadBeginning);
        books.add(bookReptileRoom);
        books.add(bookWideWindow);
        books.add(bookMiserableMill);

        given(bookRepository.findAll()).willReturn(books);

        // When
        List<BookResponseDto> foundBooks = bookService.findAll();

        // Then
        assertThat(foundBooks.size()).isEqualTo(4);
        assertThat(foundBooks.get(0).getSubtitle()).isEqualTo("The Bad Beginning");
        assertThat(foundBooks.get(1).getSubtitle()).isEqualTo("The Reptile Room");
        assertThat(foundBooks.get(2).getSubtitle()).isEqualTo("The Wide Window");
        assertThat(foundBooks.get(3).getSubtitle()).isEqualTo("The Miserable Mill");
    }

    @Test
    public void findAll_Books_NonExistingBooks_Return_EmptyListBookResponseDTO() {
        // Given
        List<Book> books = new ArrayList<>();

        given(bookRepository.findAll()).willReturn(books);

        // When
        List<BookResponseDto> foundBooks = bookService.findAll();

        // Then
        assertThat(foundBooks).isEmpty();
    }

    @Test
    public void findById_Book_ExistingBook_Return_BookResponseDTO() {
        // Given
        long bookId = 1L;

        given(bookRepository.findById(bookId)).willReturn(Optional.of(bookBadBeginning));

        // When
        BookResponseDto foundBook = bookService.findById(bookId);

        // Then
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getId()).isEqualTo(bookId);
        assertThat(foundBook.getTitle()).isEqualTo("A Series of Unfortunate Events");
        assertThat(foundBook.getSubtitle()).isEqualTo("The Bad Beginning");
        assertThat(foundBook.getTotalPages()).isEqualTo(176);
        assertThat(foundBook.getIsbn13()).isEqualTo("9780064407663");
    }

    @Test
    public void findById_Book_NonExistingBook_Return_Null() {
        // Given
        long bookId = 10L;

        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        BookResponseDto foundBook = bookService.findById(bookId);

        // Then
        assertThat(foundBook).isNull();
    }

    @Test
    public void save_Book_NonExistingBook_Return_BookResponseDTO() {
        // Given
        given(bookRepository.existsByIsbn13(bookSaveRequestAustereAcademy.getIsbn13())).willReturn(false);
        given(bookRepository.save(bookToSaveAustereAcademy)).willReturn(bookSavedAustereAcademy);

        // When
        BookResponseDto savedBook = bookService.save(bookSaveRequestAustereAcademy);

        // Then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("A Series of Fortunate Events");
        assertThat(savedBook.getSubtitle()).isEqualTo("The Ostentatious Academy");
        assertThat(savedBook.getTotalPages()).isEqualTo(231);
        assertThat(savedBook.getIsbn13()).isEqualTo("9780064408639");
    }

    @Test
    public void save_Book_ExistingBook_Return_Null() {
        // Given
        given(bookRepository.existsByIsbn13(anyString())).willReturn(true);

        // When
        BookResponseDto savedBook = bookService.save(bookSaveRequestAustereAcademy);

        // Then
        assertThat(savedBook).isNull();
    }

    @Test
    public void update_Book_ExistingBook_Return_BookResponseDTO() {
        // Given
        long bookId = 5L;

        given(bookRepository.findById(bookId)).willReturn(Optional.ofNullable(bookToUpdateAustereAcademy));
        given(bookRepository.save(bookToUpdateAustereAcademy)).willReturn(bookUpdatedAustereAcademy);

        // When
        BookResponseDto updatedBook = bookService.update(bookId, bookUpdateRequestAustereAcademy);

        // Then
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo("A Series of Unfortunate Events");
        assertThat(updatedBook.getSubtitle()).isEqualTo("The Austere Academy");
        assertThat(updatedBook.getTotalPages()).isEqualTo(231);
        assertThat(updatedBook.getIsbn13()).isEqualTo("9780064408639");
    }

    @Test
    public void update_Book_NonExistingBook_Return_Null() {
        // Given
        long bookID = 10L;

        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        BookResponseDto updatedBook = bookService.update(bookID, bookUpdateRequestAustereAcademy);

        // Then
        assertThat(updatedBook).isNull();
    }

    @Test
    public void deleteById_Book_NoReturn() {
        // Given
        long bookID = 1L;

        willDoNothing().given(bookRepository).deleteById(anyLong());

        // When
        bookService.deleteById(bookID);

        // Then
        verify(bookRepository, times(1)).deleteById(bookID);
    }
}
