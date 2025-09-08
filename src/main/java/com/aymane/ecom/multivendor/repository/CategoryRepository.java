package com.aymane.ecom.multivendor.repository;

import com.aymane.ecom.multivendor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);
}
