package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реалізація UserService та UserDetailsService.
 * Відповідає за реєстрацію, перевірку існування користувачів та авторизацію.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    /**
     * Зберігає користувача з хешуванням паролю.
     */
    @Override
    public void saveUser(User user) {
        MDC.put("userId", user.getEmail()); // Додаємо ID користувача в MDC
        logger.debug("Saving user with email: {}", user.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getEmail());
        MDC.clear(); // Очищаємо MDC
    }

    /**
     * Перевіряє, чи вже існує користувач з вказаним email або мобільним.
     */
    @Override
    public List<Object> isUserPresent(User user) {
        MDC.put("userId", user.getEmail()); // Додаємо ID користувача в MDC
        boolean userExists = false;
        String message = null;
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserEmail.isPresent()) {
            logger.warn("Email already exists: {}", user.getEmail());
            userExists = true;
            message = "Email Already Present!";
        }
        Optional<User> existingUserMobile = userRepository.findByMobile(user.getMobile());
        if (existingUserMobile.isPresent()) {
            logger.warn("Mobile number already exists: {}", user.getMobile());
            userExists = true;
            message = "Mobile Number Already Present!";
        }
        if (existingUserEmail.isPresent() && existingUserMobile.isPresent()) {
            message = "Email and Mobile Number Both Already Present!";
        }
        logger.debug("Check complete for email={}, mobile={}", user.getEmail(), user.getMobile());
        MDC.clear(); // Очищаємо MDC
        return Arrays.asList(userExists, message);
    }

    /**
     * Завантажує користувача за email для Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MDC.put("userId", email); // Додаємо ID користувача в MDC
        logger.debug("Attempting to load user by email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> {
            logger.error("User not found with email: {}", email);
            MDC.clear(); // Очищаємо MDC перед винятком
            return new UsernameNotFoundException(
                    String.format("User with email %s not found", email)
            );
        });
    }

    /**
     * Пошук користувачів за префіксом email (для автозаповнення).
     */
    @Override
    public List<User> findUsersByEmailPrefix(String emailPrefix) {
        MDC.put("userId", emailPrefix); // Додаємо ID користувача в MDC
        logger.debug("Finding users with email prefix: {}", emailPrefix);
        List<User> users = userRepository.findByEmailStartingWith(emailPrefix);
        logger.info("Found {} users matching prefix '{}'", users.size(), emailPrefix);
        MDC.clear(); // Очищаємо MDC
        return users;
    }
}
