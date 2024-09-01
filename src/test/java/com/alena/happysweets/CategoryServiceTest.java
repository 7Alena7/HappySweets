package com.alena.happysweets;

import com.alena.happysweets.exceptions.CategoryDeletionException;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.model.Category;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.repository.CategoryRepository;
import com.alena.happysweets.repository.ProductRepository;
import com.alena.happysweets.service.CategoryService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock//annotation used to create mock objects
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks//injecting mocked dependencies
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Verifies that getAllCategory() returns the expected list of categories.
    @Test
    void testGetAllCategory() {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategory();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    //Ensures that a category is saved correctly
    @Test
    void testAddCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        categoryService.addCategory(category);

        verify(categoryRepository, times(1)).save(category);
    }

    //Tests successful category deletion when no products are associated with it
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testRemoveCategoryById(int id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Category " + id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.findAllByCategoryId(id)).thenReturn(Collections.emptyList());

        categoryService.removeCategoryById(id);

        verify(categoryRepository, times(1)).delete(category);
    }

    //Tests behavior when trying to delete a category with associated products
    @Test
    void testRemoveCategoryByIdThrowsCategoryDeletionException() {
        int id = 1;
        Category category = new Category();
        Product product = new Product();
        List<Product> products = Collections.singletonList(product);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.findAllByCategoryId(id)).thenReturn(products);

        assertThrows(CategoryDeletionException.class, () -> categoryService.removeCategoryById(id));
        verify(categoryRepository, times(1)).findById(id);
        verify(productRepository, times(1)).findAllByCategoryId(id);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    //Tests behavior when a category is not found
    @Test
    void testRemoveCategoryByIdThrowsResourceNotFoundException() {
        int id = 1;
        Category category = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(productRepository.findAllByCategoryId(id)).thenReturn(Collections.emptyList());

        categoryService.removeCategoryById(id);

        verify(categoryRepository, times(1)).findById(id);
        verify(productRepository, times(1)).findAllByCategoryId(id);
        verify(categoryRepository, times(1)).delete(category);
    }

    //Tests retrieval of a category by ID
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testGetCategoryById(int id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Category " + id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategoryById(id);

        assertTrue(foundCategory.isPresent());
        assertEquals(id, foundCategory.get().getId());
        verify(categoryRepository, times(1)).findById(id);
    }
}
