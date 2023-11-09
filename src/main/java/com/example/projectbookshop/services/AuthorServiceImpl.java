package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;
import com.example.projectbookshop.repositories.AuthorRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {

        List<Author> authors = authorRepository.findAll();

        List<AuthorDTO> dtos = new ArrayList<>();

        for(Author author : authors){
            List<String> books = null;
            for(Book book : author.getBooks()){
                books.add(book.getName());
            }

            AuthorDTO newAuthor = AuthorDTO.builder()
                    .firstName(author.getFirstName())
                    .lastName(author.getLastName())
                    .age(author.getAge())
                    .id(author.getId())
                    .booksNames(books)
                    .build();
            dtos.add(newAuthor);
        }

        return dtos;

    }

    @Override
    public AuthorDTO getAuthorById(Long authorId) throws NotFoundException {
        Author author = authorRepository.findById(authorId).orElseThrow( () -> new NotFoundException("Author with id: " +
                authorId + " doesn't exist"));

        List<String> books = null;
        for(Book book : author.getBooks()){
            books.add(book.getName());
        }

        AuthorDTO dto = AuthorDTO.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .age(author.getAge())
                .id(author.getId())
                .booksNames(books)
                .build();

        return dto;
    }

    @Override
    public AuthorDTO saveNewAuthor(AuthorDTO authorDTO) {
        Author author = Author.builder()
                .firstName(authorDTO.getFirstName())
                .lastName(authorDTO.getLastName())
                .age(authorDTO.getAge())
                .build();

        Author saved = authorRepository.save(author);

        AuthorDTO dto = AuthorDTO.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .age(saved.getAge())
                .build();

        return dto;
    }

    @Override
    public void deleteAuthorById(Long authorId) throws NotFoundException {

        Author author = authorRepository.findById(authorId).orElseThrow( () -> new NotFoundException("Author with id: " +
                authorId + " doesn't exist"));

        authorRepository.delete(author);
    }
}
