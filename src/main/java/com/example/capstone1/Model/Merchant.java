package com.example.capstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "Merchant ID cannot be empty")
    @Size(min = 2, max = 10, message = "Merchant ID must be between 2 and 10 letters")
    private String ID;
    @NotEmpty(message = "Merchant name cannot be empty")
    @Size(min = 4, max = 20, message ="Merchant name can only be between 4 and 20 letters")
    private String name;

}
