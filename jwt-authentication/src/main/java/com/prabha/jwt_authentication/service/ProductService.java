package com.prabha.jwt_authentication.service;

import com.prabha.jwt_authentication.entity.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Product getProductById(Long id);

    Page<Product> getAllProducts(int page, int size, String sortBy, String direction);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max);

    List<Product> getLowStockProducts(Integer threshold);
}
