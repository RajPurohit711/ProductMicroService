package com.example.productService.service;

import com.example.productService.entity.Category;
import com.example.productService.entity.Product;

import java.util.List;

public interface CategoryService {

    void add(Category product);
    Category get(String id);
    List<Category> getAll();
    void delete(String id);

}
