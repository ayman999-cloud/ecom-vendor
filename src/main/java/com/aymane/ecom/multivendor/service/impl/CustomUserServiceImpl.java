package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.domain.UserRole;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.SellerRepository;
import com.aymane.ecom.multivendor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            final String actualUsername = username.substring(SELLER_PREFIX.length());
            final Seller seller = sellerRepository.findByEmail(actualUsername);
            if (seller != null) {
                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
            }
        } else {
            final User user = userRepository.findByEmail(username);
            if (Objects.nonNull(user)) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("User or seller not found with email " + username);
    }

    private UserDetails buildUserDetails(final String email,
                                         final String password,
                                         UserRole role) {
        if (Objects.isNull(role)) {
            role = UserRole.CUSTOMER;
        }

        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}
