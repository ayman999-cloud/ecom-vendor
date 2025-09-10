package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.CartItem;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws IllegalAccessException;
    public Cart findUserCart(User user) throws IllegalAccessException;
}
