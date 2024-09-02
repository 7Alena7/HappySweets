package com.alena.happysweets.service;

import com.alena.happysweets.exceptions.CategoryDeletionException;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.model.Category;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.CategoryRepository;
import com.alena.happysweets.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
//Implementations of all the category methods
@Service
public class CategoryService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
    public void addCategory(Category category){
        categoryRepository.save(category);
    }
    public void removeCategoryById(int id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Product> products = productRepository.findAllByCategoryId(id);

        if (!products.isEmpty()) {
            throw new CategoryDeletionException("Cannot delete category with existing products.");
        }

        categoryRepository.delete(category);
    }
    public Optional<Category> getCategoryById(int id){
        return categoryRepository.findById(id);
    }/*method may return a Category object, but it might also return
     an empty result if no Category is found with the given ID.*/
}
