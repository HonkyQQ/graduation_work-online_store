package org.example.onlinestore.service;

import org.example.onlinestore.entity.Review;
import org.example.onlinestore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Получить отзывы для продукта с заданной сортировкой.
     */
    public List<Review> getReviewsByProductId(Long productId, String sortType) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (sortType != null) {
            switch (sortType.toLowerCase()) {
                case "rating_desc":
                    reviews = reviews.stream()
                            .sorted(Comparator.comparingInt(Review::getRating).reversed())
                            .collect(Collectors.toList());
                    break;

                case "rating_asc":
                    reviews = reviews.stream()
                            .sorted(Comparator.comparingInt(Review::getRating))
                            .collect(Collectors.toList());
                    break;

                case "date_desc":
                    reviews = reviews.stream()
                            .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                            .collect(Collectors.toList());
                    break;

                case "date_asc":
                    reviews = reviews.stream()
                            .sorted(Comparator.comparing(Review::getCreatedAt))
                            .collect(Collectors.toList());
                    break;

                default:
                    throw new IllegalArgumentException("Неправильный тип сортировки: " + sortType);
            }
        }

        return reviews;
    }

    /**
     * Создать новый отзыв.
     */
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Обновить существующий отзыв.
     */
    public Review updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id).map(review -> {
            review.setRating(updatedReview.getRating());
            review.setComment(updatedReview.getComment());
            review.setProduct(updatedReview.getProduct());
            review.setUser(updatedReview.getUser());
            return reviewRepository.save(review);
        }).orElse(null);
    }

    /**
     * Удалить отзыв по ID.
     */
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    /**
     * Получить отзыв по ID.
     */
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }
}