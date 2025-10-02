package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        System.out.println("=== DASHBOARD ACCESSED ===");

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("User: " + username + " is authenticated");

            User user = userService.findByUsername(username);
            if (user != null) {
                System.out.println("User role: " + user.getRole());
                model.addAttribute("user", user);
                return "dashboard"; // Always return the dashboard template
            }
        }

        // If not authenticated, redirect to login
        return "redirect:/login";
    }
}