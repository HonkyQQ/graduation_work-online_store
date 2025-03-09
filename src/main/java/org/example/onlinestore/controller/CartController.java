package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Product;
import org.example.onlinestore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        Long userId = 1L;
        List<Product> products = cartService.getProductsInCart(userId);
        BigDecimal totalPrice = cartService.getTotalPrice(userId);

        model.addAttribute("products", products);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }
}