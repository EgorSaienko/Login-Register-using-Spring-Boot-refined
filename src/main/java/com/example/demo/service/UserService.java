package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    List<Object> isUserPresent(User user);

    List<User> findUsersByEmailPrefix(String emailPrefix);
}
