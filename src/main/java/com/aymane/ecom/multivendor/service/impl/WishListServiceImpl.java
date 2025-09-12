package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.model.WishList;
import com.aymane.ecom.multivendor.repository.WishListRepository;
import com.aymane.ecom.multivendor.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;

    @Override
    public WishList createWishList(User user) {
        final WishList wishList = new WishList();
        wishList.setUser(user);
        return this.wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
        final WishList wishList = this.wishListRepository.findByUserId(user.getId());
        return wishList == null ? createWishList(user) : wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        final WishList wishList = this.wishListRepository.findByUserId(user.getId());

        if (wishList.getProducts().contains(product)) {
            wishList.getProducts().remove(product);
        } else wishList.getProducts().add(product);

        return this.wishListRepository.save(wishList);
    }
}
