package com.alena.happysweets;

import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.ProductRepository;
import com.alena.happysweets.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//Tests if the getAllProduct() method returns the correct list of products.
    @Test
    void testGetAllProduct() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProduct();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }
//Verifies that the addProduct() method correctly saves the product.
    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        productService.addProduct(product);

        verify(productRepository, times(1)).save(product);
    }

//A parameterized test that checks if the removeProductById() method deletes a product for each provided ID.
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void testRemoveProductById(long id) {
        productService.removeProductById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

//Tests if the getProductById() method returns the correct product based on the ID
    @Test
    void testGetProductById() {
        long id = 1L;
        Product product = new Product();
        product.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(id);

        assertTrue(foundProduct.isPresent());
        assertEquals(id, foundProduct.get().getId());
        verify(productRepository, times(1)).findById(id);
    }
//A parameterized test that verifies if the getAllProductsByCategoryId() method returns the correct list of products for each category ID
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetAllProductsByCategoryId(int categoryId) {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAllByCategoryId(categoryId)).thenReturn(products);

        List<Product> result = productService.getAllProductsByCategoryId(categoryId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAllByCategoryId(categoryId);
    }
}
