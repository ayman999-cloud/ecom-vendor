package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.model.WishList;

public interface WishListService {
    WishList createWishList(User user);

    WishList getWishListByUserId(User user);

    WishList addProductToWishList(User user, Product product);
}
