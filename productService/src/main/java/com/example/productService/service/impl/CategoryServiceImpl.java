package com.example.productService.service.impl;

import com.example.productService.entity.Category;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void add(Category product) {
        categoryRepository.save(product);
    }

    @Override
    public Category get(String id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}
