package org.example.onlinestore.controller;

import org.example.onlinestore.service.CategoryService;
import org.example.onlinestore.service.ProductService;
import org.example.onlinestore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService; // Добавил сервис отзывов

    @Autowired
    public PageController(ProductService productService, CategoryService categoryService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/catalog/{id}")
    public String catalogByCategory(@PathVariable Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getProductsByCategory(id));
        return "catalog";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.searchProducts(query));
        return "catalog";
    }

}