package com.aymane.ecom.multivendor.service;

import com.aymane.ecom.multivendor.controller.request.CreateReviewRequest;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Review;
import com.aymane.ecom.multivendor.model.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest req, User user, Product product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;
}
