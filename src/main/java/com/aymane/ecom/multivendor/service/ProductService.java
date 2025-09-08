package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.controller.request.CreateProductRequest;
import com.aymane.ecom.multivendor.exception.ProductException;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller) throws IllegalAccessException;

    void deleteProduct(Long productId) throws ProductException;

    Product updateProduct(Long productId, Product product) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> searchProducts(String query);

    Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minimumPrice, Integer maximumPrice, Integer minDiscount,
                                 String sort, String stock, Integer pageNumber);

    List<Product> getProductBySellerId(Long sellerId);
}
