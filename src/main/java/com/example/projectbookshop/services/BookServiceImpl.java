package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookDTO;
import com.example.projectbookshop.repositories.AuthorRepository;
import com.example.projectbookshop.repositories.BasketRepository;
import com.example.projectbookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final BasketRepository basketRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BasketRepository basketRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.basketRepository = basketRepository;
    }

    @Override
    public List<BookDTO> listAllBooks() {
        List<Book> books = bookRepository.findAll();

        List<BookDTO> dtos = new ArrayList<>();

        for(Book book : books){
            BookDTO dto = BookDTO.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .price(book.getPrice())
                    .authorFullName(book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName())
                    .numberOfPages(book.getNumberOfPages())
                    .build();
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BookDTO createBookToAuthor(Long authorId, BookDTO bookDTO) throws NotFoundException {
        Author author = authorRepository.findById(authorId).orElseThrow( () -> new NotFoundException("Author with" +
                " id: " + authorId + " doesn't exists"));

        Book exists = bookRepository.findByName(bookDTO.getName());
        if(exists != null){
            throw new IllegalStateException("Book by name " + bookDTO.getName() + " already exists");
        }

        Book book = Book.builder()
                .author(author)
                .price(bookDTO.getPrice())
                .name(bookDTO.getName())
                .numberOfPages(bookDTO.getNumberOfPages())
                .build();

        Book saved = bookRepository.save(book);

        BookDTO dto = BookDTO.builder()
                .name(saved.getName())
                .numberOfPages(saved.getNumberOfPages())
                .id(saved.getId())
                .authorFullName(saved.getAuthor().getFirstName() + " " + saved.getAuthor().getLastName())
                .price(saved.getPrice())
                .build();

        return dto;
    }

    @Override
    public void deleteBookById(Long bookId) throws NotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow( () -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        List<Basket> baskets = basketRepository.findAll();
        if(baskets == null){
            throw new IllegalStateException("There are no baskets");
        }
        for(Basket basket : baskets){
            for (Book book1 : basket.getBooks()){
                if(book.getId() == book1.getId()){
                    throw new IllegalStateException("First remove " + book.getName() + " from all baskets then delete");
                }
            }
        }

        bookRepository.delete(book);
    }

    @Override
    public BookDTO modifyBookById(Long bookId, BookDTO bookDTO) throws NotFoundException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        if(StringUtils.hasText(bookDTO.getName())){
            book.setName(bookDTO.getName());
        }
        if(bookDTO.getPrice() != null){
            book.setPrice(bookDTO.getPrice());
        }
        if(bookDTO.getNumberOfPages() != null){
            book.setNumberOfPages(bookDTO.getNumberOfPages());
        }

        Book saved = bookRepository.save(book);

        BookDTO dto = BookDTO.builder()
                .name(saved.getName())
                .numberOfPages(saved.getNumberOfPages())
                .id(saved.getId())
                .authorFullName(saved.getAuthor().getFirstName() + " " + saved.getAuthor().getLastName())
                .price(saved.getPrice())
                .build();

        return dto;
    }

    @Override
    public List<String> getBookNames(List<Book> books) {
        List<String> bookNames = new ArrayList<>();
        for (Book book : books){
            bookNames.add(book.getName());
        }

        return bookNames;
    }

    @Override
    public List<Long> getBookIds(List<Book> books) {
        List<Long> bookIds = new ArrayList<>();
        for(Book book : books) {
            bookIds.add(book.getId());
        }

        return bookIds;
    }
}
