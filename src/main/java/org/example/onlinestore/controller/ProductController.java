package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Product;
import org.example.onlinestore.service.CategoryService;
import org.example.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/catalog")
    public String filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortPrice,
            @RequestParam(required = false) String sortRating,
            Model model) {
        List<Product> products = productService.filterProducts(category, minPrice, maxPrice, sortPrice, sortRating);

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("sortPrice", sortPrice);
        model.addAttribute("sortRating", sortRating);

        return "catalog";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/product/{productId}/average-rating")
    @ResponseBody
    public String getAverageRating(@PathVariable Long productId) {
        return String.valueOf(productService.getAverageRating(productId));
    }
}
