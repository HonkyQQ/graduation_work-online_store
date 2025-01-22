package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Review;
import org.example.onlinestore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/product/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getReviews(@PathVariable Long productId, Model model) {
        model.addAttribute("reviews", reviewService.getReviewsByProduct(productId));
        return "product";
    }

    @PostMapping
    public String addReview(
            @PathVariable Long productId,
            @RequestParam String comment,
            @RequestParam int rating,
            Principal principal) {
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);
        reviewService.addReview(review, principal.getName());
        return "redirect:/product/" + productId;
    }
}