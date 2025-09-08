package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.exception.ProductException;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long productId
    ) throws ProductException {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) String color,
                                                        @RequestParam(required = false) String size,
                                                        @RequestParam(required = false) Integer minPrice,
                                                        @RequestParam(required = false) Integer maxPrice,
                                                        @RequestParam(required = false) Integer minDiscount,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false) String stock,
                                                        @RequestParam(required = false) Integer pageNumber) {
        return ResponseEntity.ok(productService.getAllProducts(category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber));
    }
}
