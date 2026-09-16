package com.example.capstone1.Controller;

import com.example.capstone1.Api.ApiResponse;
import com.example.capstone1.Model.Category;
import com.example.capstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory(){
        return ResponseEntity.status(200).body(categoryService.getCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i<categoryService.getCategories().size(); i++){
            if (categoryService.getCategories().get(i).getID().equals(category.getID())){
                return ResponseEntity.status(400).body(new ApiResponse("This ID is already taken"));
            }
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("Category was added"));
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<?> updateCategory(@PathVariable String ID, @RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (categoryService.updateCategory(ID, category)){
            return ResponseEntity.status(200).body(new ApiResponse("Category was updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category was not found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteCategory(@PathVariable String ID){
        if (categoryService.deleteCategory(ID) == 0){
            return ResponseEntity.status(200).body(new ApiResponse("Category was deleted"));
        }
        if (categoryService.deleteCategory(ID) == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Can't delete this category it has products"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
    }




}
