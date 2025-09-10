package com.aymane.ecom.multivendor.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponse {
    private String paymentLinkUrl;
    private String paymentLinkId;
}
