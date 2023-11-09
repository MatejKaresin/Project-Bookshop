package com.example.projectbookshop.services;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    AuthorDTO saveNewAuthor(AuthorDTO authorDTO);

    void deleteAuthorById(Long authorId) throws NotFoundException;

    AuthorDTO getAuthorById(Long authorId) throws NotFoundException;

    AuthorDTO modifyAuthor(Long authorId, AuthorDTO authorDTO) throws NotFoundException;
}
