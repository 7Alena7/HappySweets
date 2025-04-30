package com.alena.happysweets.repository;

import com.alena.happysweets.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}