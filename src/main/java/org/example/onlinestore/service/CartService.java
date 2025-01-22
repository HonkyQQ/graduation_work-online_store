package org.example.onlinestore.service;

import org.example.onlinestore.entity.Cart;
import org.example.onlinestore.entity.Product;
import org.example.onlinestore.entity.User;
import org.example.onlinestore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCartForUser(userId));
    }

    public void addProductToCart(Long userId, Product product) {
        Cart cart = getCartByUserId(userId);
        cart.getProducts().add(product);
        cartRepository.save(cart);
    }

    public void removeProductFromCart(Long userId, Product product) {
        Cart cart = getCartByUserId(userId);
        if (!cart.getProducts().remove(product)) {
            throw new RuntimeException("Продукт не найден в корзине.");
        }
        cartRepository.save(cart);
    }

    public List<Product> getProductsInCart(Long userId) {
        return getCartByUserId(userId).getProducts();
    }

    public BigDecimal getTotalPrice(Long userId) {
        return getProductsInCart(userId).stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Cart createNewCartForUser(Long userId) {
        Cart cart = new Cart();
        User user = new User();
        user.setId(userId);
        cart.setUser(user);
        return cartRepository.save(cart);
    }
}