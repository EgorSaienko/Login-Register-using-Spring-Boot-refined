package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

/**
 * Інтерфейс сервісу користувача.
 */
public interface UserService {

    /**
     * Зберігає нового користувача.
     */
    void saveUser(User user);

    /**
     * Перевіряє, чи існує вже користувач з таким email або телефоном.
     */
    List<Object> isUserPresent(User user);

    /**
     * Пошук користувачів за префіксом email.
     */
    List<User> findUsersByEmailPrefix(String emailPrefix);
}

