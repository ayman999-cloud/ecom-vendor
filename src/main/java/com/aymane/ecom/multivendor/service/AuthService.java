package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.controller.response.SignupRequest;

public interface AuthService {
    String createUser(SignupRequest signupRequest) throws Exception;

    void sentLoginOtp(String email) throws Exception;

    AuthResponse signin(LoginRequest loginRequest);
}
