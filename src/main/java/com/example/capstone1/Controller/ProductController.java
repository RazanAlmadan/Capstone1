package com.example.capstone1.Controller;

import com.example.capstone1.Api.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i<productService.getProducts().size(); i++){
            if (productService.getProducts().get(i).getID().equals(product.getID())){
                return ResponseEntity.status(400).body(new ApiResponse("This ID is already taken"));
            }
        }
        if (productService.addProduct(product) == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Category ID was not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product was added"));
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<?> updateProduct(@PathVariable String ID, @RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (productService.updateProduct(ID, product)){
            return ResponseEntity.status(200).body(new ApiResponse("Product info was updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Product was not found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteProduct(@PathVariable String ID){
        if (productService.deleteProduct(ID)){
            return ResponseEntity.status(200).body(new ApiResponse("product was deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Product was not found"));
    }

    @GetMapping("/get/by/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category){
        if (productService.getProductsByCategory(category).isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No products for this category"));
        }
        return ResponseEntity.status(200).body(productService.getProductsByCategory(category));
    }

}
