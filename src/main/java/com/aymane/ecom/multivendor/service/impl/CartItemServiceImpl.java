package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.CartItem;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.CartItemRepository;
import com.aymane.ecom.multivendor.service.CartItemService;
import com.aymane.ecom.multivendor.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(cartItemId);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You cant update this cart item");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item = findCartItemById(cartItemId);

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            this.cartItemRepository.delete(item);
        } else {
            throw new Exception("You cant delete this cart item");
        }
    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return this.cartItemRepository.findById(id).orElseThrow(() -> new Exception("Cart item not found with id " + id));
    }
}
