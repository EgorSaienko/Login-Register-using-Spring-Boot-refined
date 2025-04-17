package com.example.demo.controller;

import com.example.demo.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Equipment;

/**
 * Контролер для керування адміністративною панеллю.
 * Дозволяє додавати, редагувати, призначати та видаляти обладнання.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private EquipmentService equipmentService;

    /**
     * Виводить адмінську панель з усім обладнанням.
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        logger.debug("Loading admin dashboard");
        model.addAttribute("equipments", equipmentService.getAllEquipments());
        return "admin/admin_dashboard";
    }

    /**
     * Додає нове обладнання до бази даних.
     */
    @PostMapping("/equipment/add")
    public String addEquipment(@ModelAttribute Equipment equipment) {
        MDC.put("equipmentId", equipment.getName()); // Додаємо ID обладнання в MDC
        logger.debug("Attempting to add new equipment: {}", equipment.getName());
        try {
            equipmentService.save(equipment);
            logger.info("Equipment '{}' added successfully", equipment.getName());
        } catch (Exception e) {
            logger.error("Failed to add equipment '{}': {}", equipment.getName(), e.getMessage(), e);
        }
        MDC.clear(); // Очищаємо MDC
        return "redirect:/admin/dashboard";
    }

    /**
     * Призначає обладнання користувачу.
     */
    @PostMapping("/equipment/assign")
    public String assignEquipmentToUser(@RequestParam Long equipmentId,
                                        @RequestParam Long userId,
                                        Model model) {
        MDC.put("equipmentId", String.valueOf(equipmentId)); // Додаємо ID обладнання в MDC
        MDC.put("userId", String.valueOf(userId)); // Додаємо ID користувача в MDC
        logger.debug("Assigning equipment ID={} to user ID={}", equipmentId, userId);
        try {
            equipmentService.assignEquipmentToUser(equipmentId, userId);
            logger.info("Equipment ID={} successfully assigned to user ID={}", equipmentId, userId);
        } catch (RuntimeException e) {
            logger.error("Error assigning equipment ID={} to user ID={}: {}", equipmentId, userId, e.getMessage());
            model.addAttribute("equipments", equipmentService.getAllEquipments());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin_dashboard";
        }
        MDC.clear(); // Очищаємо MDC
        return "redirect:/admin/dashboard";
    }

    /**
     * Оновлює статус обладнання.
     */
    @PostMapping("/equipment/edit")
    public String editEquipment(@RequestParam Long equipmentId, @RequestParam String status) {
        MDC.put("equipmentId", String.valueOf(equipmentId)); // Додаємо ID обладнання в MDC
        logger.debug("Updating status of equipment ID={} to '{}'", equipmentId, status);
        equipmentService.updateEquipmentStatus(equipmentId, status);
        logger.info("Status of equipment ID={} updated to '{}'", equipmentId, status);
        MDC.clear(); // Очищаємо MDC
        return "redirect:/admin/dashboard";
    }

    /**
     * Видаляє обладнання з бази даних.
     */
    @PostMapping("/equipment/delete")
    public String deleteEquipment(@RequestParam Long equipmentId) {
        MDC.put("equipmentId", String.valueOf(equipmentId)); // Додаємо ID обладнання в MDC
        logger.debug("Deleting equipment with ID={}", equipmentId);
        equipmentService.deleteEquipment(equipmentId);
        logger.info("Equipment with ID={} deleted successfully", equipmentId);
        MDC.clear(); // Очищаємо MDC
        return "redirect:/admin/dashboard";
    }
}
