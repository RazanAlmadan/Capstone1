package com.example.capstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    public Product(){

    }

    @NotEmpty(message = "Product ID cannot be empty")
    @Size(min = 2, max = 10, message = "Product ID must be between 2 and 10 letters")
    private String ID;
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 4, max = 100, message = "product name can only be between 4 and 100 letters")
    private String name;
    @NotNull(message = "Product price cannot be empty")
    @Positive(message = "Product price can only be a positive number")
    private double price;
    @NotEmpty(message = "Category ID cannot be empty")
    private String categoryID;

}
