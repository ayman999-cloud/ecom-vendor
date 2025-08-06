package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.config.JwtProvider;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.UserRepository;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public User findUserByJwtToken(final String jwt) throws Exception {
        final String email = jwtProvider.getEmailFromToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(final String email) throws Exception {
        final User user = userRepository.findByEmail(email);
        if(Objects.isNull(user)) {
            throw new Exception("User not found with email " + email);
        }
        return user;
    }
}
