package com.example.demo.controller;

import com.example.demo.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Equipment;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("equipments", equipmentService.getAllEquipments());
        return "admin/admin_dashboard";
    }

    @PostMapping("/equipment/add")
    public String addEquipment(@ModelAttribute Equipment equipment) {
        System.out.println("DEBUG: Trying to add equipment: " + equipment);
        try {
            equipmentService.save(equipment);
            System.out.println("DEBUG: Equipment added successfully");
        } catch (Exception e) {
            e.printStackTrace();  // Виведе повний стек помилки
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/assign")
    public String assignEquipmentToUser(@RequestParam Long equipmentId,
                                        @RequestParam Long userId,
                                        Model model) {
        try {
            equipmentService.assignEquipmentToUser(equipmentId, userId);
        } catch (RuntimeException e) {
            model.addAttribute("equipments", equipmentService.getAllEquipments()); // щоб не було null
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin_dashboard"; // тут твій шаблон головної сторінки
        }

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/edit")
    public String editEquipment(@RequestParam Long equipmentId, @RequestParam String status) {
        equipmentService.updateEquipmentStatus(equipmentId, status);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/delete")
    public String deleteEquipment(@RequestParam Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return "redirect:/admin/dashboard";
    }

}

