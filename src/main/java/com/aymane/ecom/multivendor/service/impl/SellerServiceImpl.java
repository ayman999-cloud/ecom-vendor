package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.config.JwtProvider;
import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.repository.SellerRepository;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Seller getSellerProfile(final String jwt) throws Exception {
        final String sellerEmail = this.jwtProvider.getEmailFromToken(jwt);
        if (Strings.isEmpty(sellerEmail)) {
            throw new Exception("Token not valid");
        }
        return this.getSellerByEmail(sellerEmail);
    }

    @Override
    public Seller createSeller(final Seller seller) throws Exception {
        final Seller sellerExist = this.sellerRepository.findByEmail(seller.getEmail());
        if (Objects.nonNull(sellerExist)) {
            throw new Exception("Seller already exist, use different email");
        }
        return this.sellerRepository.save(seller);
    }

    @Override
    public Seller gerSellerById(Long id) {
        return null;
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        final Seller seller = this.sellerRepository.findByEmail(email);
        if (Objects.isNull(seller)) {
            throw new Exception("User not found ...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return List.of();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) {
        return null;
    }

    @Override
    public void deleteSeller(Long id) {

    }

    @Override
    public Seller verifyEmail(String email, String otp) {
        return null;
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        return null;
    }
}
