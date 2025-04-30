package com.alena.happysweets.controller;

import com.alena.happysweets.dto.ProductDTO;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.model.Category;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.service.CategoryService;
import com.alena.happysweets.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    private final CategoryService categoryService;
    private final ProductService productService;


    @GetMapping("/admin")
    public String getAdminHome(Model model) {
        model.addAttribute("LowProfitProducts", productService.getProductsWithLowPriceWeightRatio());
        model.addAttribute("MightBeTooExpensiveProducts", productService.getProductsWithHighPriceWeightRatio());
        return "admin_home";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        logger.info("All the categories had been pulled from the database");
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categories_add";
    }

    @PostMapping("/admin/categories/add")
    public String postCategoriesAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        logger.info("New category had been created");
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        logger.debug("Category had been deleted");
        return "redirect:/admin/categories";
    }

    @GetMapping("admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        logger.debug("Trying to get a category with given id");
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categories_add";
        } else {
            logger.warn("Category not found, redirection to 404");
            return "404";
        }
    }

    @GetMapping("/admin/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String getProductsAdd(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        logger.debug("Model categories had been created and all the categories from database had been added to it");
        return "products_add";
    }

    @PostMapping("/admin/products/add")
    public String postProductAdd(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") MultipartFile file,
                                 @RequestParam("imageName") String imageName) throws IOException {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        Optional<Category> categoryOptional = categoryService.getCategoryById(productDTO.getCategoryId());
        if (categoryOptional.isPresent()) {
            product.setCategory(categoryOptional.get());
        } else {
            logger.warn("Category doesn't exist, throwing ResourceNotFoundException");
            throw new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId());
        }
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());
        String imageIdentifier;
        if (!file.isEmpty()) {
            imageIdentifier = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageIdentifier);
            Files.write(fileNameAndPath, file.getBytes());
            logger.debug("Adding image, it should be saved into productImages");
        } else {
            imageIdentifier = imageName;
        }
        product.setImageName(imageIdentifier);
        productService.addProduct(product);
        logger.info("productAdded");
        return "redirect:/admin/products";
    }

    @GetMapping("admin/product/delete/{id}")
    public String getDeleteProduct(@PathVariable("id") long id) {
        productService.removeProductById(id);
        logger.info("product has been removed");
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProduct(@PathVariable("id") long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        Product product;
        if (productOptional.isPresent()) {
            product = productOptional.get();
        } else {
            logger.warn("product with id not found, throwing ResourceNotFoundException");
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        logger.info("product has been updated");

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);

        return "products_add";
    }
}