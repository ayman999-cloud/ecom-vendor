package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.request.CreateReviewRequest;
import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Review;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.service.ProductService;
import com.aymane.ecom.multivendor.service.ReviewService;
import com.aymane.ecom.multivendor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(
            @PathVariable Long productId
    ) {
        final List<Review> reviews = this.reviewService.getReviewByProductId(productId);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest createReviewRequest,
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long productId
    ) throws Exception {

        final User user = this.userService.findUserByJwtToken(jwt);

        final Product product = this.productService.findProductById(productId);

        final Review review = this.reviewService.createReview(createReviewRequest, user, product);

        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest request,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        final Review review = this.reviewService.updateReview(reviewId, request.getReviewText(), request.getReviewRating(), user.getId());

        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        final User user = this.userService.findUserByJwtToken(jwt);

        this.reviewService.deleteReview(reviewId, user.getId());

        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Review deleted successfully");

        return ResponseEntity.ok(apiResponse);
    }
}
