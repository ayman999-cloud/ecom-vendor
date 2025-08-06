package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.model.Seller;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller gerSellerById(Long id);
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Long id, Seller seller);
    void deleteSeller(Long id);
    Seller verifyEmail(String email, String otp);
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);
}
