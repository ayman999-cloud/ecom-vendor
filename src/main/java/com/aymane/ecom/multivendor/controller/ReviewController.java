package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.service.ProductService;
import com.aymane.ecom.multivendor.service.ReviewService;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;


}
