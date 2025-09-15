package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.model.Cart;
import com.aymane.ecom.multivendor.model.Coupon;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.service.CartService;
import com.aymane.ecom.multivendor.service.CouponService;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/coupon")
@RestController
public class AdminCouponController {
    private final CartService cartService;
    private final UserService userService;
    private final CouponService couponService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestParam String apply,
            @RequestParam String code,
            @RequestParam double orderValue,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        Cart cart;

        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        return new ResponseEntity<>(this.couponService.createCoupon(coupon), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception {
        this.couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(this.couponService.findAllCoupons());
    }
}
