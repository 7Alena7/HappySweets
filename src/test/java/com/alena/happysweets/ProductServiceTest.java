package com.alena.happysweets;


import com.alena.happysweets.dto.ProductDTO;
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
//GetTopFourMostExpensiveProducts made with custom query test
    @Test
    public void testGetTopFourMostExpensiveProducts() {
        List<Product> products = getProducts();

        when(productRepository.findTopFourExpensiveProducts()).thenReturn(products);

        List<Product> topProducts = productService.getTopFourExpensiveProducts();

        assertEquals(4, topProducts.size());
        assertEquals("Product 2", topProducts.get(0).getName());
        assertEquals("Product 1", topProducts.get(1).getName());
        assertEquals("Product 3", topProducts.get(2).getName());
        assertEquals("Product 4", topProducts.get(3).getName());

        verify(productRepository, times(1)).findTopFourExpensiveProducts();
    }

    private static List<Product> getProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(25);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(30);

        Product product3 = new Product();
        product3.setName("Product 3");
        product3.setPrice(15);

        Product product4 = new Product();
        product4.setName("Product 4");
        product4.setPrice(13);

        Product product5 = new Product();
        product5.setName("Product 5");
        product5.setPrice(5);

        List<Product> products = Arrays.asList(product2, product1, product3, product4);
        return products;
    }
    @Test
    void testGetProductsWithLowPriceWeightRatio() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(10.00);
        product1.setWeight(5.00);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(6.50);
        product2.setWeight(4.00);

        List<Product> mockProducts = Arrays.asList(product1, product2);
        when(productRepository.findLowPriceWeightRatio()).thenReturn(mockProducts);

        List<ProductDTO> result = productService.getProductsWithLowPriceWeightRatio();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("2.000", result.get(0).getPriceWeightRatio());
        assertEquals("Product 2", result.get(1).getName());
        assertEquals("1.625", result.get(1).getPriceWeightRatio());
    }
    @Test
    void testGetProductsWithHighPriceWeightRatio() {
        Product product1 = new Product();
        product1.setName("Product 3");
        product1.setPrice(50.00);
        product1.setWeight(2.00);

        Product product2 = new Product();
        product2.setName("Product 4");
        product2.setPrice(20.00);
        product2.setWeight(1.00);

        List<Product> mockProducts = Arrays.asList(product1, product2);
        when(productRepository.findHighPriceWeightRatio()).thenReturn(mockProducts);

        List<ProductDTO> result = productService.getProductsWithHighPriceWeightRatio();

        assertEquals(2, result.size());
        assertEquals("Product 3", result.get(0).getName());
        assertEquals("25.000", result.get(0).getPriceWeightRatio());
        assertEquals("Product 4", result.get(1).getName());
        assertEquals("20.000", result.get(1).getPriceWeightRatio());
    }
}
