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

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    // Отримання всього обладнання
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    // Додавання нового обладнання
    public void addEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    // Призначення обладнання користувачу
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

    // Оновлення статусу обладнання
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

    // Отримання обладнання, призначеного конкретному користувачу
    public List<Equipment> getUserEquipments(String email) {
        return equipmentRepository.findByAssignedUser_Email(email);
    }

    // Додавання коментаря до обладнання
    public void addComment(Long equipmentId, String commentText) {
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Comment comment = new Comment(equipmentOpt.get(), commentText);
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Обладнання не знайдено");
        }
    }
    public void save(Equipment equipment) {
        // Перевіряємо, чи є purchase_date
        if (equipment.getPurchaseDate() == null) {
            equipment.setPurchaseDate(LocalDate.now()); // Встановлюємо поточну дату, якщо значення відсутнє
        }
        equipmentRepository.save(equipment);
    }
    public void deleteEquipment(Long equipmentId) {
        if (equipmentRepository.existsById(equipmentId)) {
            equipmentRepository.deleteById(equipmentId);
        } else {
            throw new RuntimeException("Обладнання не знайдено");
        }
    }

}
