package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.config.JwtProvider;
import com.aymane.ecom.multivendor.controller.request.LoginRequest;
import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.model.SellerReport;
import com.aymane.ecom.multivendor.model.VerificationCode;
import com.aymane.ecom.multivendor.repository.VerificationCodeRepository;
import com.aymane.ecom.multivendor.service.AuthService;
import com.aymane.ecom.multivendor.service.EmailService;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.aymane.ecom.multivendor.utils.OtpUtil.generateOtp;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody final LoginRequest loginRequest) {
        loginRequest.setEmail("seller_" + loginRequest.getEmail());
        return ResponseEntity.ok(this.authService.signin(loginRequest));
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        final VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong otp ...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody final Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);
        final String otp = generateOtp();
        final VerificationCode verificationCodeNew = new VerificationCode();
        verificationCodeNew.setOtp(otp);
        verificationCodeNew.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCodeNew);

        final String subject = "Email verification code";
        final String text = "Welcome, please verify your account: http://localhost:3000/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), otp, subject, text);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSeller);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable final Long sellerId) throws Exception {
        final Seller seller = this.sellerService.getSellerById(sellerId);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") final String jwt) throws Exception {
        final Seller seller = this.sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) final AccountStatus accountStatus) {
        List<Seller> allSellers = this.sellerService.getAllSellers(accountStatus);
        return ResponseEntity.ok(allSellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller
    ) throws Exception {
        final Seller sellerProfile = this.sellerService.getSellerProfile(jwt);
        final Seller updatedSeller = this.sellerService.updateSeller(sellerProfile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable final Long id) throws Exception {
        this.sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }


}
