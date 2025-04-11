package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Реалізація UserService та UserDetailsService.
 * Відповідає за реєстрацію, перевірку існування користувачів та авторизацію.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    /**
     * Зберігає користувача з хешуванням паролю.
     */
    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
//        user.setRole(Role.USER);
        userRepository.save(user);
    }

    /**
     * Перевіряє, чи вже існує користувач з вказаним email або мобільним.
     */
    @Override
    public List<Object> isUserPresent(User user) {
        boolean userExists = false;
        String message = null;
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserEmail.isPresent()) {
            userExists = true;
            message = "Email Already Present!";
        }
        Optional<User> existingUserMobile = userRepository.findByMobile(user.getMobile());
        if (existingUserMobile.isPresent()) {
            userExists = true;
            message = "Mobile Number Already Present!";
        }
        if (existingUserEmail.isPresent() && existingUserMobile.isPresent()) {
            message = "Email and Mobile Number Both Already Present!";
        }
        System.out.println("existingUserEmail.isPresent() - " + existingUserEmail.isPresent() +
                "existingUserMobile.isPresent() - " + existingUserMobile.isPresent());
        return Arrays.asList(userExists, message);
    }

    /**
     * Завантажує користувача за email для Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND", email)
                ));
    }

    /**
     * Пошук користувачів за префіксом email (для автозаповнення).
     */
    @Override
    public List<User> findUsersByEmailPrefix(String emailPrefix) {
        return userRepository.findByEmailStartingWith(emailPrefix);
    }
}
