package com.alena.happysweets.repository;

import com.alena.happysweets.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ProductRepository extends JpaRepository <Product, Long>{
    //Method gets a list of products with a given Category id
    List<Product> findAllByCategoryId(int id);
    @Query(value = "SELECT * FROM PRODUCTS ORDER BY PRICE DESC LIMIT 4", nativeQuery = true)
    List<Product> findTopFourExpensiveProducts();
}
