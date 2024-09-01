package com.alena.happysweets.repository;

import com.alena.happysweets.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /*The CategoryRepository interface is needed to provide a mechanism to perform
    CRUD operations and custom queries on Category entities without the need
    to manually implement these operations.
    By extending JpaRepository, you leverage Spring Data JPA's powerful features,
    which significantly reduces the amount of boilerplate code in your application.
     */
}
