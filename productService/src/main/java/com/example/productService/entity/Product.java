package com.example.productService.entity;

import com.example.productService.dto.MerchantProductDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;

@Document
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private boolean allowed;
    private String description;
    private String category;

    private List<MerchantProductDto> merchantProducts;

    public void  addMerchantProduct(MerchantProductDto merchantProductDto){
        merchantProducts.add(merchantProductDto);
    }

    public void sortMerchantProduct(){
        merchantProducts.sort(Comparator.comparing(MerchantProductDto::getPrice));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public List<MerchantProductDto> getMerchantProducts() {
        return merchantProducts;
    }

    public void setMerchantProducts(List<MerchantProductDto> merchantProducts) {
        this.merchantProducts = merchantProducts;
    }
}
