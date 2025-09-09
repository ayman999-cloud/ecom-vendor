package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.CartItem;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.CartItemRepository;
import com.aymane.ecom.multivendor.repository.CartRepository;
import com.aymane.ecom.multivendor.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) throws IllegalAccessException {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if (isPresent == null) {
            final CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);

            int totalMrpPrice = quantity * product.getMrpPrice();
            cartItem.setMrpPrice(totalMrpPrice);

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) throws IllegalAccessException {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        cart.setTotalSellingPrice(totalDiscountedPrice);

        return cart;
    }

    private int calculateDiscountPercentage(final int mrpPrice, final int sellingPrice) throws IllegalAccessException {
        if (mrpPrice <= 0) {
            return 0;
        }
        return 100 * (mrpPrice - sellingPrice) / mrpPrice;
    }
}
