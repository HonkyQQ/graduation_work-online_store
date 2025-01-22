package org.example.onlinestore.service;

import org.example.onlinestore.entity.Order;
import org.example.onlinestore.entity.User;
import org.example.onlinestore.repository.OrderRepository;
import org.example.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Создание нового заказа.
     *
     * @param order  Данные заказа.
     * @param userId ID пользователя (может быть null).
     * @return Созданный заказ.
     */
    public Order createOrder(Order order, Long userId) {
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден."));
            order.setUser(user);
        }
        order.setCreatedAt(LocalDateTime.now());
        validateOrder(order);
        return orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isEmpty()) {
            throw new RuntimeException("Адрес доставки не может быть пустым.");
        }
        if (order.getRecipientName() == null || order.getRecipientName().isEmpty()) {
            throw new RuntimeException("Имя получателя не может быть пустым.");
        }
        if (order.getPhoneNumber() == null || order.getPhoneNumber().isEmpty()) {
            throw new RuntimeException("Номер телефона не может быть пустым.");
        }
    }
}