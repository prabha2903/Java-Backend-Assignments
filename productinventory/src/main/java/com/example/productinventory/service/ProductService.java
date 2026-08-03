package com.example.productinventory.service;

import com.example.productinventory.entity.Product;
import com.example.productinventory.exception.DuplicateProductException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Transactional
    public Product addProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new DuplicateProductException("Product already exists with name: " + product.getName());
        }
        product.setId(null);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);

        if (!existing.getName().equalsIgnoreCase(product.getName())
                && productRepository.existsByName(product.getName())) {
            throw new DuplicateProductException("Product already exists with name: " + product.getName());
        }

        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setQuantity(product.getQuantity());
        existing.setSupplierName(product.getSupplierName());

        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product existing = getProductById(id);
        productRepository.delete(existing);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRange(Double min, Double max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Both min and max price must be provided");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min price cannot be greater than max price");
        }
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        if (threshold == null || threshold < 0) {
            throw new IllegalArgumentException("threshold must be a non-negative number");
        }
        return productRepository.findByQuantityLessThan(threshold);
    }
}
