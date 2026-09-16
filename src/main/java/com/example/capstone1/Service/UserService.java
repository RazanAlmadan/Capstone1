package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {

   ArrayList<User> users = new ArrayList<>();
   ArrayList<User> usersWithGiftCard = new ArrayList<>();
   ArrayList<User> frozenUsers = new ArrayList<>();

   private final ProductService productService;
   private final MerchantService merchantService;
   private final MerchantStockService merchantStockService;


   public ArrayList<User> getUsers(){
       return users;
   }

   public void addUser(User user){
       users.add(user);
   }

   public boolean updateUser(String ID, User user){
       for (int i = 0; i<users.size(); i++){
           if (users.get(i).getID().equals(ID)){
               users.set(i, user);
               return true;
           }
       }
       return false;
   }

   public boolean deleteUser(String ID){
       for (int i = 0; i<users.size(); i++){
           if (users.get(i).getID().equals(ID)){
               users.remove(i);
               return true;
           }
       }
       return false;
   }

   public int buyProduct(String userID, String productID, String merchantID, int quantity) {
       // 1. Validate Product ID
       int productIndex = -1;
       for (int i = 0; i < productService.getProducts().size(); i++) {
           if (productService.getProducts().get(i).getID().equals(productID)) {
               productIndex = i;
               break;
           }
       }
       if (productIndex == -1) return 1;

       // 2. Validate Merchant ID
       boolean isValidMerchant = false;
       for (int i = 0; i < merchantService.getMerchants().size(); i++) {
           if (merchantService.getMerchants().get(i).getID().equals(merchantID)) {
               isValidMerchant = true;
               break;
           }
       }
       if (!isValidMerchant) return 2;

       // 3. Validate Stock Availability
       int stockIndex = -1;
       for (int i = 0; i < merchantStockService.getMerchantStocks().size(); i++) {
           if (merchantStockService.getMerchantStocks().get(i).getProductID().equals(productID) &&
                   merchantStockService.getMerchantStocks().get(i).getMerchantID().equals(merchantID)) {
               stockIndex = i;
               break;
           }
       }
       if (stockIndex == -1 || merchantStockService.getMerchantStocks().get(stockIndex).getStock() < quantity) {
           return 3; // Out of stock or invalid stock record
       }

       // 4. Validate Customer User
       int userIndex = -1;
       for (int i = 0; i < users.size(); i++) {
           if (users.get(i).getID().equals(userID) && "Customer".equalsIgnoreCase(users.get(i).getRole())) {
               userIndex = i;
               break;
           }
       }
       if (userIndex == -1) return 4;

       // 5. Calculate Price and Balance
       double originalPrice = productService.getProducts().get(productIndex).getPrice() * quantity;
       double finalPrice = originalPrice;
       boolean hasGiftCard = usersWithGiftCard.contains(users.get(userIndex));

       if (hasGiftCard && finalPrice <= 50) {
           finalPrice = 0; // Fully covered by gift card
       }

       if (users.get(userIndex).getBalance() < finalPrice) {
           return 5; // Insufficient balance
       }

       // Deduct Stock and Balance
       merchantStockService.getMerchantStocks().get(stockIndex).setStock(merchantStockService.getMerchantStocks().get(stockIndex).getStock() - quantity);
       users.get(userIndex).setBalance(users.get(userIndex).getBalance() - finalPrice);

       if (hasGiftCard) {
           usersWithGiftCard.remove(users.get(userIndex));
       }

       return 0;
   }


    /// user can increase their balance unless they are frozen
    public int addBalance(String userID, int amount){
       if (amount < 50){
           return 1;
       }

       String customerID = "";
       int customerIndex = -1;
        for (int i = 0; i<users.size(); i++){
           if (users.get(i).getID().equals(userID)){
               customerID = users.get(i).getID();
                customerIndex = i;
           }
       }
        if (customerIndex == -1){
            return 2;
        }
        boolean isFrozen = false;
        for (int i = 0; i<frozenUsers.size(); i++){
            if (frozenUsers.get(i).getID().equals(customerID)){
                isFrozen = true;
                return 3;
            }
        }
        if (!isFrozen){
            users.get(customerIndex).setBalance(users.get(customerIndex).getBalance() + amount);
            return 0;
        }
       return -1;
    }

    /// bay a gift card for another user
    public int giveGiftCard(String giverID, String giftedID){
        boolean isValidGiverID = false;
        boolean isValidGiftedID = false;
        int giverIndex = 0;
        int giftedIndex = 0;
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getID().equals(giverID)){
                isValidGiverID = true;
                giverIndex = i;
            }
            if (users.get(i).getID().equals(giftedID)){
                isValidGiftedID = true;
                giftedIndex = i;
            }
        }

        if (!isValidGiftedID || !isValidGiverID){
            return -1;
        }

        if (users.get(giverIndex).getBalance() < 50){
            return 1;
        }

        users.get(giverIndex).setBalance(users.get(giverIndex).getBalance() - 50);
        usersWithGiftCard.add(users.get(giftedIndex));
        return 0;
    }

    /// get recommendations based on a category a user want
    public ArrayList<Product> getRecommendationsByCategory(String userID, String categoryName){
        ArrayList<Product> productForThisCategory = productService.getProductsByCategory(categoryName);
        double userBalance = -1;
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getID().equals(userID)){
                userBalance = users.get(i).getBalance();
                break;
            }
        }
        if (userBalance == -1){
            return null;
        }
        ArrayList<Product> temp = new ArrayList<>();
        for (int i = 0; i<productForThisCategory.size(); i++){
            if (productForThisCategory.get(i).getPrice()<=userBalance){
                temp.add(productForThisCategory.get(i));
            }
        }
        return temp;
    }

    /// get recommendations
    public ArrayList<Product> getRecommendations(String userID){
        double userBalance = -1;
        for (int i = 0; i<users.size(); i++){
            if (users.get(i).getID().equals(userID)){
                userBalance = users.get(i).getBalance();
                break;
            }
        }
        if (userBalance == -1){
            return null;
        }
        ArrayList<Product> temp = new ArrayList<>();
        for (int i = 0; i<productService.getProducts().size(); i++){
            if (productService.getProducts().get(i).getPrice()<=userBalance){
                temp.add(productService.getProducts().get(i));
            }
        }
        return temp;
    }

    public int addDiscountToAllProducts(String userID, double discount) {
        //Check Admin
        boolean isAdmin = false;
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(userID.trim()) && u.getRole().trim().equalsIgnoreCase("Admin")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) return 1;

        if (discount < 0.1 || discount > 0.95) return 2;

        // if a discount is active, restore old prices first so we don't save a discounted price!
        if (!productService.getOldPrices().isEmpty()) {
            deleteDiscount(userID);
        }

        //Save original prices into oldPrices as fresh copies
        for (Product p : productService.getProducts()) {
            Product backup = new Product();
            backup.setID(p.getID());
            backup.setName(p.getName());
            backup.setPrice(p.getPrice()); // Pure original price
            backup.setCategoryID(p.getCategoryID());

            productService.getOldPrices().add(backup);
        }

        // 5. Apply percentage off: e.g., 0.2 discount means (1 - 0.2) = 80% of original price
        for (Product p : productService.getProducts()) {
            double newPrice = p.getPrice() * (1 - discount);
            p.setPrice(newPrice);
        }

        return 0; // Success
    }

    public int deleteDiscount(String userID) {
        // 1. Check Admin
        boolean isAdmin = false;
        for (User u : users) {
            if (u.getID().trim().equalsIgnoreCase(userID.trim()) && u.getRole().trim().equalsIgnoreCase("Admin")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) return 1;


        if (productService.getOldPrices().isEmpty()) return 2;

        //Restore exact original prices back
        for (int i = 0; i < productService.getProducts().size(); i++) {
            Product currentProduct = productService.getProducts().get(i);
            Product originalBackup = productService.getOldPrices().get(i);

            currentProduct.setPrice(originalBackup.getPrice());
        }


        productService.getOldPrices().clear();

        return 0; // Success
    }

    /// Admin can freeze a user
    public int freezeUser(String adminID, String customerID) {
        boolean isAdmin = false;
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(adminID) && u.getRole().equalsIgnoreCase("Admin")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) return 1;


        User customer = null;
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(customerID) && u.getRole().equalsIgnoreCase("Customer")) {
                customer = u;
                break;
            }
        }
        if (customer == null) return 2; // Customer not found

        // Create a backup User copy with original balance before resetting
        User frozenCopy = new User();
        frozenCopy.setID(customer.getID());
        frozenCopy.setUserName(customer.getUserName());
        frozenCopy.setBalance(customer.getBalance()); // Keep old balance!
        frozenCopy.setRole(customer.getRole());

        frozenUsers.add(frozenCopy);

        //Setuser balance to 0
        customer.setBalance(0.0);

        return 0; // Success
    }

    /// Admin can unfreeze a user
    public int unfreezeUser(String adminID, String customerID) {
        boolean isAdmin = false;
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(adminID) && u.getRole().equalsIgnoreCase("Admin")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) return 1;


        User activeCustomer = null;
        for (User u : users) {
            if (u.getID().equalsIgnoreCase(customerID)) {
                activeCustomer = u;
                break;
            }
        }
        if (activeCustomer == null) return 2;

        // Find Customer in frozen list
        User frozenCustomer = null;
        int frozenIndex = -1;
        for (int i = 0; i < frozenUsers.size(); i++) {
            if (frozenUsers.get(i).getID().equalsIgnoreCase(customerID)) {
                frozenCustomer = frozenUsers.get(i);
                frozenIndex = i;
                break;
            }
        }
        if (frozenCustomer == null) return 3; // Account not frozen

        // Restore original balance and remove from frozen list
        activeCustomer.setBalance(frozenCustomer.getBalance());
        frozenUsers.remove(frozenIndex);

        return 0; // Success
    }


}
