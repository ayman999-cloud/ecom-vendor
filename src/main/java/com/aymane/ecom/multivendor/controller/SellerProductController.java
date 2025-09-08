package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.request.CreateProductRequest;
import com.aymane.ecom.multivendor.exception.ProductException;
import com.aymane.ecom.multivendor.exception.SellerException;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.service.ProductService;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {
    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt) throws SellerException {
        Seller seller = sellerService.getSellerProfile(jwt);

        return ResponseEntity.ok(productService.getProductBySellerId(seller.getId()));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader("Authorization") String jwt) throws SellerException, IllegalAccessException {
        final Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(productService.createProduct(request, seller), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product) throws ProductException {
        return new ResponseEntity<>(productService.updateProduct(productId, product), HttpStatus.OK);
    }
}
