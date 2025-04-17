package com.example.demo.controller;

import com.example.demo.model.Equipment;
import com.example.demo.service.EquipmentService;
import com.example.demo.service.UserService;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Контролер для взаємодії з користувачем.
 * Дозволяє бачити призначене обладнання, залишати коментарі та шукати користувачів за email.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Відображає панель користувача з його обладнанням.
     */
    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        logger.debug("Loading user dashboard for user: {}", username);
        List<Equipment> userEquipments = equipmentService.getUserEquipments(username);
        model.addAttribute("userEquipments", userEquipments);
        return "user_dashboard";
    }

    /**
     * Додає коментар до обладнання.
     */
    @PostMapping("/comment")
    public String leaveComment(@RequestParam Long equipmentId, @RequestParam String comment) {
        logger.debug("Adding comment to equipment ID={}", equipmentId);
        try {
            equipmentService.addComment(equipmentId, comment);
            logger.info("Successfully added comment to equipment ID={}", equipmentId);
        } catch (Exception e) {
            logger.error("Error while adding comment to equipment ID={}: {}", equipmentId, e.getMessage(), e);
        }
        return "redirect:/user/dashboard";
    }

    /**
     * Пошук користувачів за email (використовується для автопідказки).
     */
    @GetMapping("/search")
    @ResponseBody
    public List<Map<String, Object>> searchUsersByEmail(@RequestParam("query") String query) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (query.length() >= 4) {
            List<User> users = userService.findUsersByEmailPrefix(query);
            for (User user : users) {
                logger.debug("Found user with email: {}", user.getEmail());
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("email", user.getEmail());
                result.add(map);
            }
            logger.info("Found {} users with email prefix '{}'", result.size(), query);
        } else {
            logger.warn("Search query too short: {}", query);
        }
        return result;
    }
}
