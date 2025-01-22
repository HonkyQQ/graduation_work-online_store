package org.example.onlinestore.service;

import org.example.onlinestore.entity.Review;
import org.example.onlinestore.entity.User;
import org.example.onlinestore.repository.ReviewRepository;
import org.example.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductIdSortedByDateDesc(productId);
    }

    public Review addReview(Review review, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        review.setUser(user);
        return reviewRepository.save(review);
    }
}