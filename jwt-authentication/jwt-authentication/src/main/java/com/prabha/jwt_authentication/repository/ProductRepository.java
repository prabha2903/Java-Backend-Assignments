package com.prabha.jwt_authentication.repository;

import com.prabha.jwt_authentication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByQuantityLessThan(Integer threshold);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductName(String productName);
}
