package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.controller.request.LoginRequest;
import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.controller.response.SignupRequest;
import com.aymane.ecom.multivendor.domain.UserRole;

public interface AuthService {
    String createUser(SignupRequest signupRequest) throws Exception;

    void sentLoginOtp(String email, UserRole userRole) throws Exception;

    AuthResponse signin(LoginRequest loginRequest);
}
