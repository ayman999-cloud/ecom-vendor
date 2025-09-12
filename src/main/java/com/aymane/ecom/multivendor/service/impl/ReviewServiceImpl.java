package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.controller.request.CreateReviewRequest;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Review;
import com.aymane.ecom.multivendor.model.User;
import com.aymane.ecom.multivendor.repository.ReviewRepository;
import com.aymane.ecom.multivendor.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        final Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);

        return this.reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return this.reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        final Review review = this.getReviewById(reviewId);

        if (!review.getUser().getId().equals(userId)) {
            throw new Exception("You can't delete this review");
        }

        review.setRating(rating);
        review.setReviewText(reviewText);

        return this.reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        final Review review = getReviewById(reviewId);

        if (!review.getUser().getId().equals(userId)) {
            throw new Exception("You can't delete this review");
        }
        this.reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return this.reviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
    }
}
