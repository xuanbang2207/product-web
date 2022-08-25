package com.home.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.home.domain.Category;
import com.home.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    
    @Query("SELECT p FROM Product p WHERE p.category.categoryId=?1")
    List<Product> findByCategoryIdLike(Long categoryId);

    List<Product> findByNameIn(Collection<String> name);
    
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
    List<Product> findByCategoryCategoryId(Long categoryId);
   

}
