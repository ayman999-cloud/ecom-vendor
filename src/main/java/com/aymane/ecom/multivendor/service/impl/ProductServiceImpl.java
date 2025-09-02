package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.controller.request.CreateProductRequest;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.repository.CategoryRepository;
import com.aymane.ecom.multivendor.repository.ProductRepository;
import com.aymane.ecom.multivendor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product findProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> searchProducts() {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minimumPrice, Integer maximumPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        return null;
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return List.of();
    }
}
