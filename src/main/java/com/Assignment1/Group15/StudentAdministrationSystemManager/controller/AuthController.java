package com.Assignment1.Group15.StudentAdministrationSystemManager.controller;

import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.User;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Student;
import com.Assignment1.Group15.StudentAdministrationSystemManager.entity.Subject;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.UserService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.StudentService;
import com.Assignment1.Group15.StudentAdministrationSystemManager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    // Process registration - SIMPLIFIED VERSION
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String role,
            Model model) {

        try {
            // Just register the user for now - we'll handle student assignment separately
            User user = userService.registerUser(username, password, email, role);

            // Simple student creation without Biology assignment for now
            if ("STUDENT".equals(role)) {
                String studentId = "F6BIO" + String.format("%03d", user.getId());
                // Using username as temporary first/last name
                Student student = studentService.createStudent(user, studentId, username, "Student");
            }

            model.addAttribute("success", "Registration successful! You can now login.");
            return "register";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}