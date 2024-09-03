package com.alena.happysweets.service;

import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//Implementation of all the product methods
@Service
public class ProductService {
    ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    public void addProduct(Product product){
        productRepository.save(product);
    }
    public void removeProductById(long id){
        productRepository.deleteById(id);
    }
    public Optional<Product> getProductById(long id){
        return productRepository.findById(id);
    }
    public List<Product> getAllProductsByCategoryId(int id){
        return productRepository.findAllByCategoryId(id);
    }
    public List<Product> getTopFourExpensiveProducts(){
        return productRepository.findTopFourExpensiveProducts();
    }
}
