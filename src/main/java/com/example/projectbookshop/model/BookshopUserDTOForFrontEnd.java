package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookshopUserDTOForFrontEnd {

    private Long id;

    @NotEmpty(message = "Nickname can't be empty")
    private String nickName;
    private String password;
    private Boolean logged;
}
