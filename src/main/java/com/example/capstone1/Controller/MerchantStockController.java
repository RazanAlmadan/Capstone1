package com.example.capstone1.Controller;

import com.example.capstone1.Api.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStock(){
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i<merchantStockService.getMerchantStocks().size(); i++){
            if (merchantStockService.getMerchantStocks().get(i).getID().equals(merchantStock.getID())){
                return ResponseEntity.status(400).body(new ApiResponse("This ID is already taken"));
            }
        }
        int result = merchantStockService.addMerchantStock(merchantStock);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Product ID was not found"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant ID was not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock was added"));
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable String ID, @RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (merchantStockService.updateMerchantStock(ID, merchantStock) == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Product ID was not found"));
        }

        if (merchantStockService.updateMerchantStock(ID,merchantStock) == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Merchant ID was not found"));
        }

        if(merchantStockService.updateMerchantStock(ID, merchantStock) == 0){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock was updated"));
        }

        return ResponseEntity.status(400).body(new ApiResponse("Merchant Stock was not found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String ID){
        if (merchantStockService.deleteMerchantStock(ID)){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock was deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant stock was not found"));
    }

    @PutMapping("/more/stock/{productID}/{merchantID}/{amount}")
    public ResponseEntity<?> addMoreStocks(@PathVariable String productID, @PathVariable String merchantID, @PathVariable int amount){
        int result = merchantStockService.addMoreStocks(productID, merchantID, amount);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid Merchant ID"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid Product ID"));
        }
        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("No Merchant Stock Was found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Stock amount was updated"));
    }

    @GetMapping("/get/low/stock")
    public ResponseEntity<?> getLowStockProducts(){
        if (merchantStockService.getLowStockProducts().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No products with low stock"));
        }
        return ResponseEntity.status(200).body(merchantStockService.getLowStockProducts());
    }



    @GetMapping("/get/by/merchantID/{merchantID}")
    public ResponseEntity<?> getMerchantStockByMerchantID(@PathVariable String merchantID){
        if (merchantStockService.getMerchantStockByMerchantID(merchantID).isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("There is no stock for this merchant"));
        }
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStockByMerchantID(merchantID));
    }

}
