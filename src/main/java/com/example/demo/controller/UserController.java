package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.service.EquipmentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

import java.util.Map;
import java.util.HashMap;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UserRepository userRepository;

    // Панель користувача
    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Equipment> userEquipments = equipmentService.getUserEquipments(username);
        model.addAttribute("userEquipments", userEquipments);
        return "user_dashboard";
    }

    // Додавання коментаря
    @PostMapping("/comment")
    public String leaveComment(@RequestParam Long equipmentId, @RequestParam String comment) {
        equipmentService.addComment(equipmentId, comment);
        return "redirect:/user/dashboard";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    @ResponseBody
    public List<Map<String, Object>> searchUsersByEmail(@RequestParam("query") String query) {
        List<User> users = query.length() < 4 ? new ArrayList<>() : userService.findUsersByEmailPrefix(query);

        // ⬇️ Створюємо легку відповідь у форматі {id, email}
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("email", user.getEmail());
            result.add(map);
        }
        return result;
    }

}

