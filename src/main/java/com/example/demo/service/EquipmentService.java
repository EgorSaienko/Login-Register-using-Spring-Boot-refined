package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Equipment;
import com.example.demo.model.EquipmentStatus;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.EquipmentRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервіс для управління обладнанням.
 */
@Service
public class EquipmentService {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentService.class);

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<Equipment> getAllEquipments() {
        logger.debug("Retrieving all equipment entries");
        return equipmentRepository.findAll();
    }

    public void addEquipment(Equipment equipment) {
        logger.debug("Adding new equipment: {}", equipment.getName());
        equipmentRepository.save(equipment);
        logger.info("Equipment added successfully: {}", equipment.getName());
    }

    public void assignEquipmentToUser(Long equipmentId, Long userId) {
        MDC.put("equipmentId", String.valueOf(equipmentId));
        MDC.put("userId", String.valueOf(userId));
        logger.info("Starting assignment of equipment ID={} to user ID={}", equipmentId, userId);
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (equipmentOpt.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            logger.error("[{}] Equipment with ID={} not found", errorId, equipmentId);
            throw new RuntimeException("Обладнання з ID " + equipmentId + " не знайдено. Reference: " + errorId);
        }
        if (userOpt.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            logger.error("[{}] User with ID={} not found", errorId, userId);
            throw new RuntimeException("Користувач з ID " + userId + " не знайдено. Reference: " + errorId);
        }

        Equipment equipment = equipmentOpt.get();
        User user = userOpt.get();

        logger.debug("Assigning equipment '{}' to user '{}'", equipment.getName(), user.getEmail());
        equipment.setAssignedUser(user);
        equipmentRepository.save(equipment);
        logger.info("Equipment ID={} assigned to user ID={}", equipmentId, userId);
        MDC.clear();
    }

    public void updateEquipmentStatus(Long equipmentId, String status) {
        MDC.put("equipmentId", String.valueOf(equipmentId));
        MDC.put("newStatus", status);
        logger.debug("Updating the status of equipment ID={} to '{}'", equipmentId, status);
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Equipment equipment = equipmentOpt.get();
            try {
                EquipmentStatus newStatus = EquipmentStatus.valueOf(status);
                equipment.setStatus(newStatus);
                equipmentRepository.save(equipment);
                logger.info("Equipment ID={} status updated to '{}'", equipmentId, status);
            } catch (IllegalArgumentException e) {
                String errorId = UUID.randomUUID().toString();
                logger.error("[{}] Invalid status '{}' for equipment ID={}", errorId, status, equipmentId);
                throw new RuntimeException("Невірний статус: " + status + ". Reference: " + errorId);
            }
        } else {
            String errorId = UUID.randomUUID().toString();
            logger.error("[{}] Equipment with ID={} not found", errorId, equipmentId);
            throw new RuntimeException("Обладнання не знайдено. Reference: " + errorId);
        }
        MDC.clear();
    }

    public List<Equipment> getUserEquipments(String email) {
        MDC.put("userEmail", email);
        logger.debug("Retrieving equipment for user with email={}", email);
        List<Equipment> list = equipmentRepository.findByAssignedUserEmail(email);
        MDC.clear();
        return list;
    }

    public void addComment(Long equipmentId, String commentText) {
        MDC.put("equipmentId", String.valueOf(equipmentId));
        logger.debug("Adding comment to equipment ID={}", equipmentId);
        Optional<Equipment> equipmentOpt = equipmentRepository.findById(equipmentId);
        if (equipmentOpt.isPresent()) {
            Comment comment = new Comment(equipmentOpt.get(), commentText);
            commentRepository.save(comment);
            logger.info("Comment added to equipment ID={}", equipmentId);
        } else {
            String errorId = UUID.randomUUID().toString();
            logger.error("[{}] Cannot add comment. Equipment with ID={} not found", errorId, equipmentId);
            throw new RuntimeException("Обладнання не знайдено. Reference: " + errorId);
        }
        MDC.clear();
    }

    public void save(Equipment equipment) {
        MDC.put("equipmentName", equipment.getName());
        logger.debug("Saving equipment '{}'", equipment.getName());
        if (equipment.getPurchaseDate() == null) {
            equipment.setPurchaseDate(LocalDate.now());
            logger.info("Purchase date not provided. Set to current date for equipment '{}'", equipment.getName());
        }
        equipmentRepository.save(equipment);
        logger.info("Equipment '{}' saved successfully", equipment.getName());
        MDC.clear();
    }

    public void deleteEquipment(Long equipmentId) {
        MDC.put("equipmentId", String.valueOf(equipmentId));
        logger.debug("Attempting to delete equipment ID={}", equipmentId);
        if (equipmentRepository.existsById(equipmentId)) {
            equipmentRepository.deleteById(equipmentId);
            logger.info("Equipment ID={} deleted successfully", equipmentId);
        } else {
            String errorId = UUID.randomUUID().toString();
            logger.error("[{}] Cannot delete. Equipment with ID={} not found", errorId, equipmentId);
            throw new RuntimeException("Обладнання не знайдено. Reference: " + errorId);
        }
        MDC.clear();
    }
}