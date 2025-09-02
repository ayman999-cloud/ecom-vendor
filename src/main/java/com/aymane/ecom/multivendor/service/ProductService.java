package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.controller.request.CreateProductRequest;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest req, Seller seller);

    void deleteProduct(Long productId);

    Product updateProduct(Long productId, Product product);

    Product findProductById(Long productId);

    List<Product> searchProducts();

    Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minimumPrice, Integer maximumPrice, Integer minDiscount,
                                 String sort, String stock, Integer pageNumber);

    List<Product> getProductBySellerId(Long sellerId);
}
