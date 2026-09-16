package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final ProductService productService;
    private final MerchantService merchantService;

    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    public ArrayList<MerchantStock> getMerchantStocks(){
        return merchantStocks;
    }

    public int addMerchantStock(MerchantStock merchantStock){
        boolean isValidProductID = false;
        for (int i = 0; i<productService.getProducts().size(); i++){
            if (productService.getProducts().get(i).getID().equals(merchantStock.getProductID())){
                isValidProductID = true;
            }
        }
        if(!isValidProductID){
            return 1;
        }
        boolean isValidMerchantID = false;
        for (int i = 0; i<merchantService.getMerchants().size(); i++){
            if (merchantService.getMerchants().get(i).getID().equals(merchantStock.getMerchantID())){
                isValidMerchantID = true;
            }
        }
        if (!isValidMerchantID){
            return 2;
        }
        if (isValidProductID && isValidMerchantID){
            merchantStocks.add(merchantStock);
        }
        return 0;
    }

    public int updateMerchantStock(String ID, MerchantStock merchantStock){
        boolean isValidProductID = false;
        for (int i = 0; i<productService.getProducts().size(); i++){
            if (productService.getProducts().get(i).getID().equals(merchantStock.getProductID())){
                isValidProductID = true;
            }
        }
        if(!isValidProductID){
            return 1;
        }
        boolean isValidMerchantID = false;
        for (int i = 0; i<merchantService.getMerchants().size(); i++){
            if (merchantService.getMerchants().get(i).getID().equals(merchantStock.getMerchantID())){
                isValidMerchantID = true;
            }
        }
        if (!isValidMerchantID){
            return 2;
        }
        if (isValidProductID && isValidMerchantID){
            for (int i = 0; i<merchantStocks.size(); i++){
                if (merchantStocks.get(i).getID().equals(ID)){
                    merchantStocks.set(i, merchantStock);
                    return 0;
                }
            }
        }
        return -1;
    }

    public boolean deleteMerchantStock(String ID){
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getID().equals(ID)){
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }

    public int addMoreStocks(String productID, String merchantID, int amount){
        boolean isValidMerchantID = false;
        for (int i = 0; i<merchantService.getMerchants().size(); i++){
            if (merchantService.getMerchants().get(i).getID().equals(merchantID)){
                isValidMerchantID = true;
            }
        }
        if (!isValidMerchantID){
            return 1;
        }
        boolean isValidProductID = false;
        for (int i = 0; i<productService.getProducts().size(); i++){
            if (productService.getProducts().get(i).getID().equals(productID)){
                isValidProductID = true;
            }
        }
        if (!isValidProductID){
            return 2;
        }
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getMerchantID().equals(merchantID) && merchantStocks.get(i).getProductID().equals(productID)){
                merchantStocks.get(i).setStock(merchantStocks.get(i).getStock() + amount);
                return 0;
            }
        }
        return -1;
    }

    /// get products with low stock
    public ArrayList<MerchantStock> getLowStockProducts(){
        ArrayList<MerchantStock> temp = new ArrayList<>();
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getStock() < 10){
                temp.add(merchantStocks.get(i));
            }
        }
        return temp;
    }


    /// Get Merchant with the cheapest price for a product
    public Merchant getCheapestMerchantForAProduct(String productName){
        Product product = productService.getCheapestProductPrice(productName);
        if (product == null){
            return null;
        }
        String merchantID = "";
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getProductID().equals(product.getID())){
                merchantID = merchantStocks.get(i).getMerchantID();
                break;
            }
        }
        for (int i = 0; i<merchantService.getMerchants().size(); i++){
            if (merchantService.getMerchants().get(i).getID().equals(merchantID)){
                return merchantService.getMerchants().get(i);
            }
        }
        return null;
    }

    /// Trade products with another merchant
    public boolean tradeMerchantProducts(String merchantID1, String merchantID2){
        String merchantStockID1 = "";
        int index1 = 0;
        String merchantStockID2 = "";
        int index2 =0;
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getMerchantID().equals(merchantID1)){
                merchantStockID1 = merchantStocks.get(i).getID();
                index1 = i;
            }
            if (merchantStocks.get(i).getMerchantID().equals(merchantID2)){
                merchantStockID2 = merchantStocks.get(i).getID();
                index2 = i;
            }
        }
        if (merchantStockID1.isEmpty() || merchantStockID2.isEmpty()){
            return false;
        }

        MerchantStock temp = merchantStocks.get(index1);
        merchantStocks.get(index2).setMerchantID(merchantID1);
        merchantStocks.get(index2).setMerchantID(temp.getMerchantID());
        return true;

    }

    /// get all merchant stock details for a merchant
    public ArrayList<MerchantStock> getMerchantStockByMerchantID(String merchantID){
        ArrayList<MerchantStock> temp = new ArrayList<>();
        for (int i = 0; i<merchantStocks.size(); i++){
            if (merchantStocks.get(i).getMerchantID().equals(merchantID)){
                temp.add(merchantStocks.get(i));
            }
        }
        return temp;
    }




}
