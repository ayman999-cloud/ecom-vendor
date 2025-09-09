package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception;

    void deleteCartItem(Long userId, Long cartItemId) throws Exception;

    CartItem findCartItemById(Long id) throws Exception;
}
