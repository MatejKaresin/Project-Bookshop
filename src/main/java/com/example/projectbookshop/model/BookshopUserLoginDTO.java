package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookshopUserLoginDTO {

    @NotEmpty(message = "Nickname can't be empty")
    private String nickName;

    @NotEmpty(message = "Password can't be empty")
    private String password;
}
