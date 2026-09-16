package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CategoryService {

    ArrayList<Category> categories = new ArrayList<>();

    public ArrayList<Category> getCategories(){
        return categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public boolean updateCategory(String ID, Category category){
        for (int i = 0; i<categories.size(); i++) {
            if (categories.get(i).getID().equals(ID)) {
                categories.set(i, category);
                return true;
            }
        }
            return false;
    }

    public int deleteCategory(String ID){
        for (int i = 0; i<categories.size(); i++) {
            if (categories.get(i).getID().equals(ID)) {
                categories.remove(i);
                return 0;
            }
        }
        return -1;
    }






}
