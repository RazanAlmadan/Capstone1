package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    public User(){

    }
    @NotEmpty(message = "User ID cannot be empty")
    @Size(min = 2, max = 10, message = "User ID can only be between 2 and 10 letters")
    private String ID;
    @NotEmpty(message = "UserName cannot be empty")
    @Size(min = 5, max = 15, message = "UserName can only be between 5 and 15 letters")
    private String userName;
    //patter
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotEmpty(message = "User Role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)$")
    private String role;
    @NotNull(message = "Balance cannot be empty")
    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;
}
