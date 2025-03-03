package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Review;
import org.example.onlinestore.service.ReviewService;
import org.example.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping
    public String getReviews(@PathVariable Long productId, Model model) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        model.addAttribute("reviews", reviews);
        return "fragments/review-list";
    }

    @GetMapping("/average-rating")
    @ResponseBody
    public String getAverageRating(@PathVariable Long productId) {
        return String.valueOf(productService.getAverageRating(productId));
    }

    @PostMapping
    public String addReview(
            @PathVariable Long productId,
            @RequestParam String comment,
            @RequestParam int rating,
            Principal principal,
            Model model) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        reviewService.addReview(comment, rating, principal.getName(), productId);
        productService.updateAverageRating(productId);

        List<Review> updatedReviews = reviewService.getReviewsByProduct(productId);
        model.addAttribute("reviews", updatedReviews);
        return "fragments/review-list :: reviewList";
    }
}
