package com.example.productService.service;

import com.example.productService.entity.Category;
import com.example.productService.entity.Product;

import java.util.List;

public interface ProductService {

    void add(Product product);
    Product get(String id);
    List<Product> getAll();

    void delete(String id);

    List<Product> findByCategory(String category);

}
