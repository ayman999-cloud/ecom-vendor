package com.aymane.ecom.multivendor.controller.request;

import com.aymane.ecom.multivendor.domain.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginOtpRequest {
    private String email;
    private String otp;
    private UserRole role;
}
