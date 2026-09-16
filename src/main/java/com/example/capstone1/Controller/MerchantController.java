package com.example.capstone1.Controller;

import com.example.capstone1.Api.ApiResponse;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchants(){
        return ResponseEntity.status(200).body(merchantService.getMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchants(@RequestBody @Valid Merchant merchant, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i<merchantService.getMerchants().size(); i++){
            if (merchantService.getMerchants().get(i).getID().equals(merchant.getID())){
                return ResponseEntity.status(400).body(new ApiResponse("This ID is already taken"));
            }
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant was added"));
    }

    @PutMapping("/update/{ID}")
    public ResponseEntity<?> updateMerchant(@PathVariable String ID, @RequestBody @Valid Merchant merchant, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (merchantService.updateMerchant(ID, merchant)){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant info was updated"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant was not found"));
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteMerchant(@PathVariable String ID){
        if (merchantService.deleteMerchant(ID)){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant was deleted"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Merchant was not found"));
    }

}
