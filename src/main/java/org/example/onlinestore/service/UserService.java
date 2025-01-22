package org.example.onlinestore.service;

import org.example.onlinestore.entity.User;
import org.example.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     */
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Проверяет, существует ли пользователь с указанным username.
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Регистрирует нового пользователя. Хэширует пароль перед сохранением.
     */
    public User registerUser(User user) {
        if (isEmailTaken(user.getEmail())) {
            throw new RuntimeException("Email уже используется.");
        }
        if (isUsernameTaken(user.getUsername())) {
            throw new RuntimeException("Имя пользователя уже используется.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Хэшируем пароль
        return userRepository.save(user);
    }

    /**
     * Авторизует пользователя по email и паролю.
     */
    public User authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        throw new RuntimeException("Неверные данные для входа.");
    }

    /**
     * Возвращает пользователя по ID.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не найден."));
    }

    /**
     * Возвращает пользователя по email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с email " + email + " не найден."));
    }
}