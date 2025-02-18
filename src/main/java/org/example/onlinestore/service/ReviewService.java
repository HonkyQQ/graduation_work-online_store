package org.example.onlinestore.service;

import org.example.onlinestore.entity.Product;
import org.example.onlinestore.entity.Review;
import org.example.onlinestore.entity.User;
import org.example.onlinestore.repository.ProductRepository;
import org.example.onlinestore.repository.ReviewRepository;
import org.example.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public void addReview(String comment, int rating, String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        review.setUser(user);
        review.setProduct(product);

        reviewRepository.save(review);
    }
}