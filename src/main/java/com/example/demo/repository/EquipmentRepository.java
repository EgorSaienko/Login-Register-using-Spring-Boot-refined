package com.example.demo.repository;

import com.example.demo.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для роботи з сутністю Equipment.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    /**
     * Повертає список обладнання, призначеного користувачу за його email.
     *
     * @param email Email користувача
     * @return список обладнання
     */
    List<Equipment> findByAssignedUserEmail(String email);
}


