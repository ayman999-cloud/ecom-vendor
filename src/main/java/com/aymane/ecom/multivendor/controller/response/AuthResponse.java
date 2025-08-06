package com.aymane.ecom.multivendor.controller.response;

import com.aymane.ecom.multivendor.domain.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String jwtToken;
    private String message;
    private UserRole role;
}
