package org.example.onlinestore.controller;

import org.example.onlinestore.entity.Order;
import org.example.onlinestore.entity.User;
import org.example.onlinestore.service.OrderService;
import org.example.onlinestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showOrderForm(@RequestParam(required = false) Long userId, Model model) {
        Order order = new Order();
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                order.setRecipientName(user.getUsername());
                order.setPhoneNumber(user.getPhoneNumber());
            }
        }
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order, @RequestParam(required = false) Long userId) {
        orderService.createOrder(order, userId);
        return "redirect:/";
    }
}