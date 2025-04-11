package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Equipment;
import com.example.demo.model.EquipmentStatus;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для управління обладнанням.
 */
@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Повертає список всього обладнання.
     */
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    /**
     * Додає нове обладнання.
     */
    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    /**
     * Призначає обладнання користувачу.
     */
    public void assignEquipmentToUser(Long equipmentId, Long userId) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (equipmentOpt.isEmpty()) {
            throw new RuntimeException("Обладнання з ID " + equipmentId + " не знайдено.");
        }
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Користувач з ID " + userId + " не знайдено.");
        }

        Equipment equipment = equipmentOpt.get();
        User user = userOpt.get();
        equipment.setAssignedUser(user);
        equipmentRepository.save(equipment);
    }

    /**
     * Оновлює статус обладнання.
     */
    public void updateEquipmentStatus(Long equipmentId, String status) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            try {
                EquipmentStatus newStatus = EquipmentStatus.valueOf(status); // Конвертуємо рядок у enum
                equipment.setStatus(newStatus);
                equipmentRepository.save(equipment);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Невірний статус: " + status);
            }
        } else {
            throw new RuntimeException("Обладнання не знайдено");
        }
    }

    /**
     * Повертає обладнання, призначене користувачу за email.
     */
    public List<Equipment> getUserEquipments(String email) {
        return equipmentRepository.findByAssignedUser_Email(email);
    }

    /**
     * Додає коментар до обладнання.
     */
    public void addComment(Long equipmentId, String commentText) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Comment comment = new Comment(equipmentOpt.get(), commentText);
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Обладнання не знайдено");
        }
    }

    /**
     * Зберігає обладнання, встановлюючи дату купівлі, якщо її не вказано.
     */
    public void save(Equipment equipment) {
        // Перевіряємо, чи є purchase_date
        if (equipment.getPurchaseDate() == null) {
            equipment.setPurchaseDate(LocalDate.now()); // Встановлюємо поточну дату, якщо значення відсутнє
        }
        equipmentRepository.save(equipment);
    }

    /**
     * Видаляє обладнання за його ID.
     */
    public void deleteEquipment(Long equipmentId) {
        if (equipmentRepository.existsById(equipmentId)) {
            equipmentRepository.deleteById(equipmentId);
        } else {
            throw new RuntimeException("Обладнання не знайдено");
        }
    }

}
