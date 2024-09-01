package com.alena.happysweets.controller;

import com.alena.happysweets.global.GlobalData;
import com.alena.happysweets.model.Product;
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
public class CartController {
    Logger logger = LoggerFactory.getLogger(CartController.class);
    ProductService productService;
    @Autowired
    public CartController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id){
        Optional<Product> product = productService.getProductById(id);
        product.ifPresent(value -> GlobalData.cart.add(value));
        logger.info("product has been added to cart");
        return"redirect:/shop";
    }
    @GetMapping("/cart")
    public String cartGet(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        logger.debug("cartCount, total, cart models have been updated");
        return "cart";
    }
    @GetMapping("/cart/removeItem/{index}")
    public String cartItemRemove(@PathVariable int index){
        GlobalData.cart.remove(index);
        logger.info("Product has been removed from the cart");
        return "redirect:/cart";
    }
    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        logger.debug("total model has been updated");
        return "checkout";
    }
}
