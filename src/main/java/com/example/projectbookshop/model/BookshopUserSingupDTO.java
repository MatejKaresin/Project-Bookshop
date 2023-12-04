package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookshopUserSingupDTO {

    @NotEmpty(message = "Nickname can't be empty")
    private String nickName;

    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;

    @NotEmpty(message = "Password can't be empty")
    private String repeatedPassword;
}
