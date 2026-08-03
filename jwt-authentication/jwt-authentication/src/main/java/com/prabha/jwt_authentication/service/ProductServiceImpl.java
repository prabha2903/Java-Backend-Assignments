package com.prabha.jwt_authentication.service;

import com.prabha.jwt_authentication.entity.Product;
import com.prabha.jwt_authentication.exception.DuplicateProductException;
import com.prabha.jwt_authentication.exception.ProductNotFoundException;
import com.prabha.jwt_authentication.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("price", "productName");

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product addProduct(Product product) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new DuplicateProductException(
                    "Product already exists with name: " + product.getProductName());
        }
        product.setProductId(null);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);

        if (!existing.getProductName().equalsIgnoreCase(product.getProductName())
                && productRepository.existsByProductName(product.getProductName())) {
            throw new DuplicateProductException(
                    "Product already exists with name: " + product.getProductName());
        }

        existing.setProductName(product.getProductName());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setSupplierName(product.getSupplierName());

        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product existing = getProductById(id);
        productRepository.delete(existing);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Page<Product> getAllProducts(int page, int size, String sortBy, String direction) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }

        String sortField = (sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy)) ? sortBy : "productId";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Both min and max price must be provided");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min price cannot be greater than max price");
        }
        return productRepository.findByPriceBetween(min, max);
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        if (threshold == null || threshold < 0) {
            throw new IllegalArgumentException("threshold must be a non-negative number");
        }
        return productRepository.findByQuantityLessThan(threshold);
    }
}
