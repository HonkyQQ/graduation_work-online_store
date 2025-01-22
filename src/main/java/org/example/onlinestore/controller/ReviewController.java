package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Review;
import org.example.onlinestore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestParam(required = false) String sortType
    ) {
        return reviewService.getReviewsByProductId(productId, sortType);
    }


    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }


    @PutMapping("/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }


    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }


    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }
}