package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.controller.response.SignupRequest;
import com.aymane.ecom.multivendor.domain.UserRole;
import com.aymane.ecom.multivendor.model.VerificationCode;
import com.aymane.ecom.multivendor.repository.UserRepository;
import com.aymane.ecom.multivendor.service.AuthService;
import com.aymane.ecom.multivendor.service.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("sign-up")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody final SignupRequest signupRequest) throws Exception {

        final String jwt = authService.createUser(signupRequest);

        return ResponseEntity.ok(AuthResponse
                .builder()
                .jwtToken(jwt)
                .message("Registered user successfully")
                .role(UserRole.CUSTOMER)
                .build());
    }

    @PostMapping("send/otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody final VerificationCode verificationCode) throws Exception {

        authService.sentLoginOtp(verificationCode.getEmail());

        return ResponseEntity.ok(ApiResponse.builder().message("OTP sent Successfully for email " +
                verificationCode.getEmail()).build());
    }

    @PostMapping("sign-in")
    public ResponseEntity<AuthResponse> sentOtpHandler(@RequestBody final LoginRequest loginRequest) throws Exception {

        final AuthResponse authResponse = authService.signin(loginRequest);

        return ResponseEntity.ok(authResponse);
    }
}
