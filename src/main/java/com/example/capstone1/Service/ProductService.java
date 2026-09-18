package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> oldPrices = new ArrayList<>();

    private final CategoryService categoryService;

    public ArrayList<Product> getProducts(){
        return products;
    }
    public ArrayList<Product> getOldPrices(){return oldPrices;}

    public int addProduct(Product product){
        boolean isValidCategoryID = false;
        for (int i = 0; i<categoryService.getCategories().size(); i++){
            if (categoryService.getCategories().get(i).getID().equals(product.getCategoryID())){
                isValidCategoryID = true;
            }
        }
        if (isValidCategoryID){
            products.add(product);
            return 0;
        }
        return 1;
    }

    public boolean updateProduct(String ID, Product product){
        for (int i = 0; i<products.size(); i++){
            if (products.get(i).getID().equals(ID)){
                products.set(i, product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String ID){
        for (int i = 0; i<products.size(); i++){
            if (products.get(i).getID().equals(ID)){
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    /// get products by category name
    public ArrayList<Product> getProductsByCategory(String category){
        ArrayList<Product> temp = new ArrayList<>();

        String categoryID = "";
        for (int i = 0; i<categoryService.getCategories().size(); i++){
            if (categoryService.getCategories().get(i).getName().equalsIgnoreCase(category)){
                categoryID = categoryService.getCategories().get(i).getID();
                break;
            }
        }

        for (int i = 0; i<products.size(); i++){
            if (products.get(i).getCategoryID().equals(categoryID)) {
                temp.add(products.get(i));
            }
        }
        return temp;
    }

    /// Search products by name
    public Product searchProductByName(String productName){
        for (int i = 0; i<products.size(); i++){
            if (products.get(i).getName().equalsIgnoreCase(productName)){
                return products.get(i);
            }
        }
        return null;
    }





}
