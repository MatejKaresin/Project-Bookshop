package com.example.projectbookshop.model;

import com.example.projectbookshop.entities.Book;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    private Long id;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    private String lastName;
    @NotNull(message = "Age can't be empty")
    private Integer age;
    private List<String> booksNames;
}
