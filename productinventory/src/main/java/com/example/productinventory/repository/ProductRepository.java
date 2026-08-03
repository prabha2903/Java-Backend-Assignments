package com.example.productinventory.repository;

import com.example.productinventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(Double min, Double max);

    List<Product> findByQuantityLessThan(Integer threshold);

    boolean existsByName(String name);

    Optional<Product> findByName(String name);
}
