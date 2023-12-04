package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookshopUserDTO {

    private Long id;

    @NotEmpty(message = "Nickname can't be empty")
    private String nickName;

    private List<String> bookNames;
}