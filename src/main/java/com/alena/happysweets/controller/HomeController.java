package com.alena.happysweets.controller;

import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.global.GlobalData;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.service.CategoryService;
import com.alena.happysweets.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    CategoryService categoryService;
    ProductService productService;
    @Autowired
    public HomeController(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping({"/","/home"})//Curly braces in the @GetMapping annotation with multiple URL patterns is a way to handle different paths with the same method
    public String home(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "index";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "shop";
    }
    @GetMapping("/shop/category/{id}")
    public String viewProductByCategory(Model model, @PathVariable("id") int id){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        logger.info("Products is shown by category");
        return "shop";
    }
    @GetMapping("shop/viewProduct/{id}")
    public String viewProduct(Model model, @PathVariable("id") int id){
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            logger.info("product is shown by id");
            return "view_product";
        } else {
            logger.warn("Product with id not found, ResourceNotFoundExceptionThrown");
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }
}
