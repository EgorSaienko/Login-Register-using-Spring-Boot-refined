package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Контролер для автентифікації та реєстрації користувачів.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserService userService;

    /**
     * Відображає сторінку входу.
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        logger.debug("Rendering login page");
        return "auth/login";
    }

    /**
     * Відображає сторінку реєстрації.
     */
    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String register(Model model) {
        logger.debug("Rendering register page");
        model.addAttribute("user", new User());
        return "auth/register";
    }

    /**
     * Обробляє POST-запит на реєстрацію нового користувача.
     */
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String registerUser(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred during registration for email: {}", user.getEmail());
            model.addAttribute("bindingResult", bindingResult);
            return "auth/register";
        }

        List<Object> userPresentObj = userService.isUserPresent(user);
        if ((Boolean) userPresentObj.get(0)) {
            logger.warn("User with email or mobile already exists: {}", user.getEmail());
            model.addAttribute("successMessage", userPresentObj.get(1));
            return "auth/register";
        }

        // Start unique error tracking
        String errorId = UUID.randomUUID().toString();
        MDC.put("errorId", errorId);
        try {
            userService.saveUser(user);
            logger.info("[{}] User registered successfully: {}", errorId, user.getEmail());
        } catch (Exception e) {
            logger.error("[{}] Error occurred during user registration: {}", errorId, e.getMessage(), e);
        } finally {
            MDC.clear();
        }

        model.addAttribute("successMessage", "User registered successfully!");
        return "auth/login";
    }
}
