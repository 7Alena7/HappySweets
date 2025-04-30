package com.alena.happysweets.service;

import com.alena.happysweets.exceptions.CategoryDeletionException;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.model.Category;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.CategoryRepository;
import com.alena.happysweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);

    }

    public void removeCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Product> products = productRepository.findAllByCategoryId(id);
        if (!products.isEmpty()) {
            throw new CategoryDeletionException("Cannot delete category with existing products.");
        }
        categoryRepository.delete(category);
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }
}