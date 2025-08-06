package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.model.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
