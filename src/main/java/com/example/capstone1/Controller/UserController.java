package com.example.capstone1.Controller;

import com.example.capstone1.Api.ApiResponse;
import com.example.capstone1.Model.User;
import com.example.capstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i<userService.getUsers().size(); i++){
            if (userService.getUsers().get(i).getID().equals(user.getID())){
                return ResponseEntity.status(400).body(new ApiResponse("This ID is already taken"));
            }
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (userService.updateUser(id, user)) {
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User ID not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User ID not found"));
    }

    @PutMapping("/buy/{userId}/{productId}/{merchantId}/{quantity}")
    public ResponseEntity<?> buyProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId, @PathVariable int quantity) {
        int result = userService.buyProduct(userId, productId, merchantId, quantity);
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Invalid Product ID"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Invalid Merchant ID"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("Product is out of stock for this merchant"));
        if (result == 4) return ResponseEntity.status(400).body(new ApiResponse("Invalid User ID or User is not a Customer"));
        if (result == 5) return ResponseEntity.status(400).body(new ApiResponse("User balance is lower than product price"));

        return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));
    }

    @PutMapping("/add/balance/{userId}/{amount}")
    public ResponseEntity<?> addBalance(@PathVariable String userId, @PathVariable int amount) {
        int result = userService.addBalance(userId, amount);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Minimum top-up amount is 50"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("User ID not found"));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Cannot add balance: Account is frozen"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Balance added successfully"));
    }

    @PutMapping("/bay/gift/card/{giverID}/{giftedID}")
    public ResponseEntity<?> giveGiftCard(@PathVariable String giverID, @PathVariable String giftedID){
        int result = userService.giveGiftCard(giverID, giftedID);
        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance to buy a gift card"));
        if (result == -1) return ResponseEntity.status(400).body(new ApiResponse("Invalid Giver or Gifted User ID"));

        return ResponseEntity.status(200).body(new ApiResponse("A gift card was given successfully"));
    }

    @GetMapping("/get/recommendations/{userID}")
    public ResponseEntity<?> getRecommendations(@PathVariable String userID){
        if (userService.getRecommendations(userID).isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Recommendations were found"));
        }
        if (userService.getRecommendations(userID) == null){
            return ResponseEntity.status(400).body(new ApiResponse("User was not found"));
        }
        return ResponseEntity.status(200).body(userService.getRecommendations(userID));
    }

    @GetMapping("/get/recommendations/by/category/{userID}/{categoryName}")
    public ResponseEntity<?> getRecommendationsByCategory(@PathVariable String userID, @PathVariable String categoryName){
        if (userService.getRecommendationsByCategory(userID, categoryName).isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Recommendations were found"));
        }
        if (userService.getRecommendations(userID) == null){
            return ResponseEntity.status(400).body(new ApiResponse("User was not found"));
        }
        return ResponseEntity.status(200).body(userService.getRecommendationsByCategory(userID, categoryName));
    }

    @PutMapping("/add/discount/{userID}/{discount}")
    public ResponseEntity<?> addDiscountToAllProducts(@PathVariable String userID, @PathVariable double discount){
        if (userService.addDiscountToAllProducts(userID, discount) == 1){
            return ResponseEntity.status(400).body(new ApiResponse("User is not an admin"));
        }
        if (userService.addDiscountToAllProducts(userID, discount) == 2){
            return ResponseEntity.status(400).body(new ApiResponse("invalid status"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Discount was added"));
    }

    @PutMapping("/delete/discount/{adminID}")
    public ResponseEntity<?> deleteDiscount(@PathVariable String adminID){
        int result = userService.deleteDiscount(adminID);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("User is not an admin"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("There was no discounts on the system"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Discount was deleted"));
    }


    @PutMapping("/freeze/{adminId}/{customerId}")
    public ResponseEntity<?> freezeUser(@PathVariable String adminId, @PathVariable String customerId) {
        int result = userService.freezeUser(adminId, customerId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Unauthorized: Provided Admin ID is invalid or not an Admin"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Customer ID not found or user is not a Customer"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Customer frozen successfully and balance set to 0"));
    }

    @PutMapping("/unfreeze/{adminId}/{customerId}")
    public ResponseEntity<?> unfreezeUser(@PathVariable String adminId, @PathVariable String customerId) {
        int result = userService.unfreezeUser(adminId, customerId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Unauthorized: Provided Admin ID is invalid or not an Admin"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Customer ID not found or user is not a Customer"));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Customer is not currently frozen"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Customer unfrozen and balance restored successfully"));
    }


}
