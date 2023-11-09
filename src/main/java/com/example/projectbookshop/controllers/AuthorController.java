package com.example.projectbookshop.controllers;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;
import com.example.projectbookshop.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public List<AuthorDTO> getAuthors(){
        return authorService.getAllAuthors();
    }

    @GetMapping("/authors/{authorId}")
    public AuthorDTO getAuthorById(@PathVariable("authorId") Long authorId) throws NotFoundException {
        return authorService.getAuthorById(authorId);
    }

    @PostMapping("/authors")
    public AuthorDTO createAuthor(@Validated @RequestBody AuthorDTO authorDTO){
        return authorService.saveNewAuthor(authorDTO);
    }

    @DeleteMapping("/authors/{authorId}")
    public void deleteAuthor(@PathVariable("authorId") Long authorId) throws NotFoundException {
        authorService.deleteAuthorById(authorId);
    }

    @PutMapping("/authors/modify/{authorId}")
    public AuthorDTO updateAuthor(@PathVariable("authorId") Long authorId,
                                  @RequestBody AuthorDTO authorDTO) throws NotFoundException {
        return authorService.modifyAuthor(authorId, authorDTO);
    }


}
