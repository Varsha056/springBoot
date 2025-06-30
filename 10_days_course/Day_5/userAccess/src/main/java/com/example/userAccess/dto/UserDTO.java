package com.example.userAccess.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter // Lombok: generates getters
@Setter // Lombok: generates setters
public class UserDTO {

    @NotBlank(message = "Username is required") // Validates field is not empty/null
    private String username;

    @NotBlank(message = "Password is required") // Validates field is not empty/null
    private String password;

}
