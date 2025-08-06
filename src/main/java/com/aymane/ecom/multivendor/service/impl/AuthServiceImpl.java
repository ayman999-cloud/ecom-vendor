package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.config.JwtProvider;
import com.aymane.ecom.multivendor.controller.response.AuthResponse;
import com.aymane.ecom.multivendor.controller.response.SignupRequest;
import com.aymane.ecom.multivendor.domain.UserRole;
import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.model.VerificationCode;
import com.aymane.ecom.multivendor.repository.CartRepository;
import com.aymane.ecom.multivendor.repository.UserRepository;
import com.aymane.ecom.multivendor.repository.VerificationCodeRepository;
import com.aymane.ecom.multivendor.service.AuthService;
import com.aymane.ecom.multivendor.service.EmailService;
import com.aymane.ecom.multivendor.service.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.aymane.ecom.multivendor.domain.UserRole.CUSTOMER;
import static com.aymane.ecom.multivendor.utils.OtpUtil.generateOtp;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomUserServiceImpl customUserService;

    @Override
    public String createUser(final SignupRequest signupRequest) throws Exception {

        final VerificationCode verificationCode = verificationCodeRepository.findByEmail(signupRequest.getEmail());

        if (Objects.isNull(verificationCode) ||
                (Objects.nonNull(verificationCode.getOtp()) && !verificationCode.getOtp().equals(signupRequest.getOtp()))) {
            throw new Exception("Wrong OTP");
        }


        User user = userRepository.findByEmail(signupRequest.getEmail());

        if (Objects.isNull(user)) {
            final User newUser = new User();
            newUser.setEmail(signupRequest.getEmail());
            newUser.setFullName(signupRequest.getFullName());
            newUser.setRole(CUSTOMER);
            newUser.setMobile("0761934549");
            newUser.setPassword(passwordEncoder.encode(signupRequest.getOtp()));

            user = userRepository.save(newUser);

            final Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(CUSTOMER.name()));
        final Authentication authentication = new UsernamePasswordAuthenticationToken(signupRequest.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public void sentLoginOtp(String email) throws Exception {
        final String SIGNIN_PREFIX = "signin_";

        if (email.startsWith(SIGNIN_PREFIX)) {
            email = email.substring(SIGNIN_PREFIX.length());
            final User user = userRepository.findByEmail(email);

            if (Objects.isNull(user)) {
                throw new Exception("User not exist with the provided email");
            }
        }

            final VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

            if (Objects.nonNull(verificationCode)) {
                verificationCodeRepository.delete(verificationCode);
            }

            final String otp = generateOtp();
            final VerificationCode verificationCodeNew = new VerificationCode();
            verificationCodeNew.setOtp(otp);
            verificationCodeNew.setEmail(email);
            verificationCodeRepository.save(verificationCodeNew);

            // Sent the verification code OTP mail
            final String subject = "OTP verified";
            final String body = "Your OTP is: " + otp;
            emailService.sendVerificationOtpEmail(
                    email,
                    otp,
                    subject,
                    body);
    }

    @Override
    public AuthResponse signin(final LoginRequest loginRequest) {
        final String username = loginRequest.getEmail();
        final String otp = loginRequest.getOtp();
        final Authentication authentication = this.authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtProvider.generateToken(authentication);
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        final String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        return AuthResponse.builder()
                .jwtToken(token)
                .role(UserRole.valueOf(role))
                .message("Login success")
                .build();
    }

    private Authentication authenticate(final String username, final String otp) {
        final UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (Objects.isNull(userDetails)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        final VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (Objects.isNull(verificationCode) || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("Invalid username or password or verification code");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
