package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.model.WishList;
import com.aymane.ecom.multivendor.service.ProductService;
import com.aymane.ecom.multivendor.service.UserService;
import com.aymane.ecom.multivendor.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<WishList> getWishListByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);
        final WishList wishList = this.wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long productId
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);
        final Product product = this.productService.findProductById(productId);

        final WishList updatedWishList = this.wishListService.addProductToWishList(user, product);

        return ResponseEntity.ok(updatedWishList);
    }
}
