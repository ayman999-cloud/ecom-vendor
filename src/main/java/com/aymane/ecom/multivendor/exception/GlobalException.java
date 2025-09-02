package com.aymane.ecom.multivendor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetail> sellerExceptionHandler(SellerException sellerException, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .error(sellerException.getMessage())
                .detail(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(errorDetail);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetail> productExceptionHandler(ProductException productException, WebRequest request) {
        ErrorDetail errorDetail = ErrorDetail.builder()
                .error(productException.getMessage())
                .detail(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(errorDetail);
    }
}
