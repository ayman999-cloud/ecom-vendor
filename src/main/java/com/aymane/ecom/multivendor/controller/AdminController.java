package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {
    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status
    ) throws Exception {

        final Seller seller = this.sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(seller);
    }

}
