package com.alena.happysweets.service;

import com.alena.happysweets.dto.ProductDTO;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final static DecimalFormat decimalFormat = new DecimalFormat("#.000");
    private final ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProductById(long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProductsByCategoryId(int id) {
        return productRepository.findAllByCategoryId(id);
    }

    public List<Product> getTopFourExpensiveProducts() {
        return productRepository.findTopFourExpensiveProducts();
    }

    public List<ProductDTO> getProductsWithLowPriceWeightRatio() {
        List<Product> products = productRepository.findLowPriceWeightRatio();
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getName(),
                        decimalFormat.format(product.getPrice() / product.getWeight())))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsWithHighPriceWeightRatio() {
        List<Product> products = productRepository.findHighPriceWeightRatio();

        return products.stream()
                .map(product -> new ProductDTO(
                        product.getName(),
                        decimalFormat.format(product.getPrice() / product.getWeight())))
                .collect(Collectors.toList());
    }
}