package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.request.AddItemRequest;
import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.CartItem;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.service.CartItemService;
import com.aymane.ecom.multivendor.service.CartService;
import com.aymane.ecom.multivendor.service.ProductService;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = this.userService.findUserByJwtToken(jwt);
        Product product = this.productService.findProductById(request.getProductId());

        CartItem cartItem = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());

        return new ResponseEntity<>(cartItem, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        this.cartItemService.deleteCartItem(user.getId(), cartItemId);
        final ApiResponse response = new ApiResponse("Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,
                                                          @RequestHeader("Authorization") String jwt,
                                                          @RequestBody CartItem cartItem) throws Exception {
        final User user = userService.findUserByJwtToken(jwt);

        CartItem item = null;

        if (cartItem.getQuantity() > 0) {
            item = this.cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
    }


}
