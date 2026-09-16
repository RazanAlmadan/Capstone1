package com.example.capstone1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "Merchant Stock ID cannot be empty")
    @Size(min = 2, max = 10, message = "Merchant Stock ID must be between 2 and 10 letters")
    private String ID;
    @NotEmpty(message = "Product ID cannot be empty")
    @Size(min = 2, max = 10, message = "Product ID must be between 2 and 10 letters")
    private String productID;
    @NotEmpty(message = "Merchant ID cannot be empty")
    @Size(min = 2, max = 10, message = "Merchant ID must be between 2 and 10 letters")
    private String merchantID;
    @NotNull(message = "Merchant Stock cannot be empty")
    @Min(value = 10, message = "Merchant Stock cannot be less than 10 at the start")
    private int stock = 10;
}
