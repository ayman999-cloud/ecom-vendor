package com.aymane.ecom.multivendor.controller.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String email;
    private String fullName;
    private String otp;
}
