package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для доступу до даних користувачів.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Пошук користувача за email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Пошук користувача за мобільним номером.
     */
    Optional<User> findByMobile(String mobile);

    /**
     * Пошук користувачів, email яких починається з певного префіксу (для автозаповнення).
     */
    List<User> findByEmailStartingWith(String prefix);
}


