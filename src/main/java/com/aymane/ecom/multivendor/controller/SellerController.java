package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.request.LoginRequest;
import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.repository.VerificationCodeRepository;
import com.aymane.ecom.multivendor.service.AuthService;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody final LoginRequest loginRequest) {
        loginRequest.setEmail("seller_" + loginRequest.getEmail());
        return ResponseEntity.ok(this.authService.signin(loginRequest));
    }

}
