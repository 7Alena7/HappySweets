package com.alena.happysweets.controller;

import com.alena.happysweets.dto.ProductDTO;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import com.alena.happysweets.model.Category;
import com.alena.happysweets.model.Product;
import com.alena.happysweets.service.CategoryService;
import com.alena.happysweets.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@Controller
public class AdminController {
   Logger logger = LoggerFactory.getLogger(AdminController.class);
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    /*System.getProperty("user.dir"):
    This method call retrieves the current working directory of the Java application.
    "user.dir" is a system property that points to the directory from which the Java application was launched.*/
    CategoryService categoryService; //Service class with business logic for retrieving categories from a database
    ProductService productService;
    @Autowired
    public AdminController(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping("/admin") //Mapping HTTP Get request to this method
    public String getAdminHome(){ //Method returns String(Which is typically a name for a html page). adminHome - just a method name(can be anything, as it does not affect the functionality).
        return "admin_home"; //Spring will look for a template file named admin_home.html (or another format like .jsp or .ftl depending on configuration)
    }
    @GetMapping("/admin/categories")//When a user visits the URL /admin/categories, this method is triggered
    public String getCategories(Model model){
        /*This line adds an attribute to the model object.
        The first parameter "categories" is the key, which will be used in the view to access this data.
        The second parameter categoryService.getAllCategory() is the value, which is the data itself.*/
        model.addAttribute("categories", categoryService.getAllCategory());
        logger.info("All the categories had been pulled from the database");
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model){
        /*We can use any word we want instead of "category"
        but it has to be the same in GET method POST method and thymeleaf template*/
        model.addAttribute("category", new Category());//Creates a new instance of the Category class.
        return "categories_add";
    }
    @PostMapping ("/admin/categories/add")//maps HTTP POST requests to the URL /admin/categories/add to this method.
    //The method is invoked when a user submits a form on the categoriesAdd page,
    public String postCategoriesAdd(@ModelAttribute("category") Category category){
        /*
        -This annotation binds the form data to the Category object.
        -When the form is submitted, Spring automatically populates the Category object with the data from the form fields.
        -The "category" here refers to the model attribute added in the getCategoriesAdd method,
        where a new Category object was added to the model.*/
        categoryService.addCategory(category);
        logger.info("New category had been created");
        return "redirect:/admin/categories";
        /*The "redirect:" prefix is used to issue a redirect response,
        which helps in avoiding duplicate form submissions,
        improving user experience by updating the browser's URL,
        and adhering to the Post/Redirect/Get pattern, which is a best practice in web development.*/
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        /*@PathVariable is a powerful feature in Spring MVC that allows to extract values
        from URL path segments and use them directly in your controller methods.
        This is especially useful for creating RESTful APIs where resource identifiers
        and other dynamic parts of the URL need to be handled.*/
        categoryService.removeCategoryById(id);
        logger.debug("Category had been deleted");
       return "redirect:/admin/categories";
    }
    @GetMapping("admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        logger.debug("Trying to get a category with given id");
        if(category.isPresent()){
            //logger.debug("Checking if the category is present");
            model.addAttribute("category", category.get());
            //logger.debug("Adding category to the model");
            /*If the category is present, this line adds the Category object to the Model with the key "category".
            This makes the Category object available to the view named "categoriesAdd"*/
            return "categories_add";
        }else{
            logger.warn("Category not found, redirection to 404");
            return "404";
        }
    }
    //Product Section
    @GetMapping("/admin/products")
        public String getProducts(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String getProductsAdd(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        logger.debug("Model categories had been created and all the categories from database had been added to it");
        /*When creating or updating a product, the admin needs to assign the product to a specific category.
        To allow the admin to choose from existing categories, the list of categories is passed to the view.
        In the HTML form, this list is used to populate a dropdown menu,
        enabling the admin to select a category for the product.*/
        return "products_add";
    }
    @PostMapping("/admin/products/add")
    public String postProductAdd(@ModelAttribute("productDTO") ProductDTO productDTO, //When the form is submitted, Spring automatically populates the fields of ProductDTO with the data from the form.
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imageName") String imageName) throws IOException{
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
            if(!file.isEmpty()){
                imageIdentifier = file.getOriginalFilename();//Retrieves the original filename of the uploaded file and assigns it to imageUUID.
                Path fileNameAndPath = Paths.get(uploadDir, imageIdentifier);// Creates a path by combining the upload directory (uploadDir) with the image filename.
                Files.write(fileNameAndPath, file.getBytes());//Writes the file bytes to the specified path, saving the uploaded image to the server.
            logger.debug("Adding image, it should be saved into productImages");
            }else{
                imageIdentifier = imageName;//If no new file is uploaded, it uses the existing image name from the hidden form field (imageName).
            }
            product.setImageName(imageIdentifier);
            productService.addProduct(product);
            logger.info("productAdded");
        return "redirect:/admin/products";
    }
    @GetMapping("admin/product/delete/{id}")
    public String getDeleteProduct(@PathVariable("id") long id){
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



